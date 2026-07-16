package pe.edu.utp.casacambio.service.transaccion.impl;

import org.springframework.beans.factory.annotation.Autowired;

import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import pe.edu.utp.casacambio.modelo.cliente.TipoOperacion;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;
import pe.edu.utp.casacambio.modelo.transaccion.DetalleTransaccion;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;
import pe.edu.utp.casacambio.modelo.notificacion.EventoSistema;
import pe.edu.utp.casacambio.repository.transaccion.TransaccionRepository;
import pe.edu.utp.casacambio.service.transaccion.TransaccionService;
import pe.edu.utp.casacambio.service.patron.decorator.CalculadorPrecio;
import pe.edu.utp.casacambio.service.patron.decorator.CalculadorPrecioBase;
import pe.edu.utp.casacambio.service.patron.decorator.ComisionDecorator;
import pe.edu.utp.casacambio.service.patron.decorator.DescuentoVipDecorator;
import pe.edu.utp.casacambio.service.patron.decorator.ImpuestoDecorator;
import pe.edu.utp.casacambio.service.patron.factory.TransaccionFactory;
import pe.edu.utp.casacambio.service.patron.observer.AuditoriaObserver;
import pe.edu.utp.casacambio.service.patron.observer.NotificacionObserver;
import pe.edu.utp.casacambio.service.patron.observer.PublicadorEventos;
import pe.edu.utp.casacambio.service.patron.singleton.GestorTipoCambio;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Implementacion de TransaccionService.
 * Orquesta la logica de negocio integrando cuatro patrones: Factory Method
 * (crea la transaccion segun el tipo), Singleton (consulta el tipo de
 * cambio vigente del dia), Decorator (calcula el precio final aplicando
 * comision, descuento VIP e impuesto de forma combinable) y Observer
 * (notifica y audita cuando una transaccion se confirma o se anula).
 * Principio SOLID - SRP: solo coordina el flujo de negocio de transacciones.
 * Principio SOLID - DIP: depende de interfaces, no de implementaciones concretas.
 */
@Service
@Slf4j
public class TransaccionServiceImpl implements TransaccionService {

    /** Repositorio de transacciones inyectado por constructor. */
    private final TransaccionRepository transaccionRepository;

    /** Subject del patron Observer: notifica confirmaciones y anulaciones. */
    private final PublicadorEventos publicadorEventos;

    /** Reloj usado para consultar la vigencia del tipo de cambio en el Singleton. */
    private final Clock clock;

    /**
     * Constructor de uso normal: crea el publicador de eventos por defecto
     * (Notificacion + Auditoria suscritos) y usa el reloj real del sistema.
     *
     * @param transaccionRepository repositorio de transacciones
     */
    @Autowired
    public TransaccionServiceImpl(TransaccionRepository transaccionRepository) {
        this(transaccionRepository, construirPublicadorPorDefecto(), Clock.system(ZoneId.of("America/Lima")));
    }

    /**
     * Constructor que permite inyectar un PublicadorEventos propio
     * (usado en pruebas unitarias para verificar que el patron Observer
     * realmente se dispara dentro del flujo de negocio). Usa el reloj
     * real del sistema.
     *
     * @param transaccionRepository repositorio de transacciones
     * @param publicadorEventos publicador de eventos a usar
     */
    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, PublicadorEventos publicadorEventos) {
        this(transaccionRepository, publicadorEventos, Clock.system(ZoneId.of("America/Lima")));
    }

    /**
     * Constructor que permite inyectar tanto el PublicadorEventos como el
     * Clock usados para consultar la vigencia del tipo de cambio. Se usa
     * en pruebas unitarias para no depender del reloj real del sistema
     * (SonarQube java:S8692), evitando que el resultado del test cambie
     * segun el dia en que se ejecute.
     *
     * @param transaccionRepository repositorio de transacciones
     * @param publicadorEventos publicador de eventos a usar
     * @param clock reloj a partir del cual se determina la vigencia del tipo de cambio
     */
    public TransaccionServiceImpl(TransaccionRepository transaccionRepository,
                                   PublicadorEventos publicadorEventos, Clock clock) {
        this.transaccionRepository = transaccionRepository;
        this.publicadorEventos = publicadorEventos;
        this.clock = clock;
    }

    private static PublicadorEventos construirPublicadorPorDefecto() {
        PublicadorEventos publicador = new PublicadorEventos();
        publicador.suscribir(new NotificacionObserver());
        publicador.suscribir(new AuditoriaObserver());
        return publicador;
    }

    /**
     * Crea una nueva transaccion usando Factory Method, valida el tipo de
     * cambio vigente usando el Singleton GestorTipoCambio y, si aplica,
     * calcula el detalle economico con el Decorator CalculadorPrecio.
     *
     * @param tipo tipo de operacion: COMPRA, VENTA o TRANSFERENCIA
     * @param cliente cliente que realiza la operacion
     * @param parMoneda par de monedas involucrado (ej: PEN/USD)
     * @param montoEntregado monto que el cliente entrega en la operacion
     * @return transaccion creada y guardada
     */
    @Override
    public Transaccion crear(TipoOperacion tipo, Cliente cliente, ParMoneda parMoneda, BigDecimal montoEntregado) {
        // Factory Method: selecciona y ejecuta el creator segun el tipo
        Transaccion transaccion = TransaccionFactory.crear(tipo);
        transaccion.setCliente(cliente);
        transaccion.setParMoneda(parMoneda);
        transaccion.setMontoEntregado(montoEntregado);

        // Una transferencia no involucra tipo de cambio entre divisas
        if (tipo != TipoOperacion.TRANSFERENCIA) {
            // Singleton: consultamos la unica instancia del gestor de tipos de cambio
            GestorTipoCambio gestor = GestorTipoCambio.getInstancia();

            if (gestor.existeTipoCambio(parMoneda, clock)) {
                TipoCambio tipoCambio = gestor.obtenerTipoCambio(parMoneda, clock);
                transaccion.setDetalle(calcularDetalle(tipo, tipoCambio, cliente, montoEntregado));
            } else {
                log.warn("No hay tipo de cambio vigente para {}", parMoneda.getPar());
            }
        }

        // Persistimos la transaccion recien creada
        return transaccionRepository.guardar(transaccion);
    }

    /**
     * Arma el detalle economico de la transaccion aplicando el patron
     * Decorator: el precio base del TipoCambio se envuelve con comision,
     * descuento VIP (solo si aplica) e impuesto, en ese orden.
     */
    private DetalleTransaccion calcularDetalle(TipoOperacion tipo, TipoCambio tipoCambio,
                                                Cliente cliente, BigDecimal montoEntregado) {
        // Decorator: se compone el calculador envolviendo capa por capa
        CalculadorPrecio calculador = new ImpuestoDecorator(
                new DescuentoVipDecorator(
                        new ComisionDecorator(
                                new CalculadorPrecioBase())));

        BigDecimal precioBase;
        BigDecimal precioFinal;
        BigDecimal montoFinal;

        if (tipo == TipoOperacion.COMPRA) {
            // El cliente entrega divisa extranjera y recibe soles
            precioBase = tipoCambio.getPrecioCompra();
            precioFinal = calculador.calcularPrecioCompra(tipoCambio, cliente);
            montoFinal = montoEntregado.multiply(precioFinal).setScale(4, RoundingMode.HALF_UP);
        } else {
            // VENTA: el cliente entrega soles y recibe divisa extranjera
            precioBase = tipoCambio.getPrecioVenta();
            precioFinal = calculador.calcularPrecioVenta(tipoCambio, cliente);
            montoFinal = montoEntregado.divide(precioFinal, 4, RoundingMode.HALF_UP);
        }

        DetalleTransaccion detalle = new DetalleTransaccion();
        detalle.setTipoCambioAplicado(precioFinal);
        detalle.setComision(precioFinal.subtract(precioBase).abs().setScale(4, RoundingMode.HALF_UP));
        detalle.setMontoFinal(montoFinal);
        detalle.calcular();
        return detalle;
    }

    /**
     * Confirma una transaccion existente cambiando su estado a COMPLETADA.
     *
     * @param id identificador de la transaccion a confirmar
     * @return transaccion confirmada
     */
    @Override
    public Transaccion confirmar(Long id) {
        // Buscamos la transaccion o lanzamos excepcion si no existe
        Transaccion transaccion = transaccionRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Transaccion no encontrada: " + id));

        // Cambiamos el estado a COMPLETADA via metodo de dominio
        transaccion.confirmar();

        // Guardamos el nuevo estado en el repositorio
        Transaccion guardada = transaccionRepository.guardar(transaccion);

        // Observer: publicamos el evento para que Notificacion y Auditoria reaccionen
        publicadorEventos.publicar(construirEvento("TRANSACCION_COMPLETADA", guardada));

        return guardada;
    }

    /**
     * Anula una transaccion existente cambiando su estado a ANULADA.
     *
     * @param id identificador de la transaccion a anular
     * @return transaccion anulada
     */
    @Override
    public Transaccion anular(Long id) {
        // Buscamos la transaccion o lanzamos excepcion si no existe
        Transaccion transaccion = transaccionRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Transaccion no encontrada: " + id));

        // Cambiamos el estado a ANULADA via metodo de dominio
        transaccion.anular();

        // Guardamos el nuevo estado en el repositorio
        Transaccion guardada = transaccionRepository.guardar(transaccion);

        // Observer: publicamos el evento para que Notificacion y Auditoria reaccionen
        publicadorEventos.publicar(construirEvento("TRANSACCION_ANULADA", guardada));

        return guardada;
    }

    /**
     * Arma el EventoSistema (sujeto del Observer) a partir de una
     * transaccion ya persistida.
     */
    private EventoSistema construirEvento(String nombre, Transaccion transaccion) {
        EventoSistema evento = new EventoSistema();
        evento.setNombre(nombre);
        evento.setDescripcion("Transaccion #" + transaccion.getId() + " - " + transaccion.getTipo());
        evento.setOrigenClase(TransaccionServiceImpl.class.getSimpleName());
        return evento;
    }

    /**
     * Busca una transaccion por su identificador unico.
     *
     * @param id identificador de la transaccion
     * @return Optional con la transaccion si existe
     */
    @Override
    public Optional<Transaccion> buscarPorId(Long id) {
        return transaccionRepository.buscarPorId(id);
    }

    /**
     * Retorna todas las transacciones registradas en el sistema.
     *
     * @return lista de transacciones
     */
    @Override
    public List<Transaccion> listarTodas() {
        return transaccionRepository.listarTodas();
    }
}
