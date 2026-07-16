package pe.edu.utp.casacambio.service.transaccion.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.casacambio.modelo.cliente.*;
import pe.edu.utp.casacambio.modelo.notificacion.EventoSistema;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;
import pe.edu.utp.casacambio.modelo.transaccion.EstadoTransaccion;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;
import pe.edu.utp.casacambio.repository.transaccion.TransaccionRepository;
import pe.edu.utp.casacambio.service.patron.observer.AuditoriaObserver;
import pe.edu.utp.casacambio.service.patron.observer.NotificacionObserver;
import pe.edu.utp.casacambio.service.patron.observer.ObservadorEvento;
import pe.edu.utp.casacambio.service.patron.observer.PublicadorEventos;
import pe.edu.utp.casacambio.service.patron.singleton.GestorTipoCambio;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link TransaccionServiceImpl}.
 *
 * A diferencia de probar la entidad Transaccion de forma aislada, estas
 * pruebas verifican el service completo: que crear() efectivamente USA
 * el Factory Method (TransaccionFactory), el Singleton (GestorTipoCambio)
 * y el Decorator (CalculadorPrecio con comision, descuento VIP e
 * impuesto) dentro del flujo de negocio real, no solo que esas clases
 * existen por separado.
 *
 * Se usa un Clock fijo, construido a partir de un instante LITERAL (sin
 * llamar a Instant.now()), tanto para armar el TipoCambio de prueba como
 * para inyectarlo en TransaccionServiceImpl. Asi el test es 100%
 * deterministico y no depende del reloj real ni del dia en que se
 * ejecute (SonarQube java:S8692).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de TransaccionServiceImpl")
class TransaccionServiceImplTest {

    private static final Clock CLOCK_FIJO =
            Clock.fixed(Instant.parse("2026-07-13T17:00:00Z"), ZoneId.of("America/Lima"));

    @Mock
    private TransaccionRepository transaccionRepository;

    private TransaccionServiceImpl transaccionService;
    private ParMoneda parPenUsd;
    private Cliente clienteNatural;
    private Cliente clienteVip;

    @BeforeEach
    void setUp() {
        transaccionService = new TransaccionServiceImpl(
                transaccionRepository, construirPublicadorPorDefecto(), CLOCK_FIJO);

        Moneda pen = new Moneda(1L, "PEN", "Sol peruano", "S/");
        Moneda usd = new Moneda(2L, "USD", "Dólar americano", "$");
        parPenUsd = new ParMoneda(1L, pen, usd, true);

        clienteNatural = new Cliente(1L, "Juan Pérez", "12345678",
                "juan@email.com", "987654321", TipoCliente.NATURAL);
        clienteVip = new Cliente(2L, "María García", "87654321",
                "maria@email.com", "999888777", TipoCliente.VIP);

        // Singleton: registramos un tipo de cambio vigente (hoy, segun CLOCK_FIJO) para PEN/USD
        TipoCambio tipoCambio = new TipoCambio(1L, parPenUsd,
                new BigDecimal("3.700"), new BigDecimal("3.800"), LocalDateTime.now(CLOCK_FIJO));
        GestorTipoCambio.getInstancia().limpiarTiposCambio();
        GestorTipoCambio.getInstancia().actualizarTipoCambio(tipoCambio);

        lenient()
                .when(transaccionRepository.guardar(any(Transaccion.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));
    }

    private static PublicadorEventos construirPublicadorPorDefecto() {
        PublicadorEventos publicador = new PublicadorEventos();
        publicador.suscribir(new NotificacionObserver());
        publicador.suscribir(new AuditoriaObserver());
        return publicador;
    }

    @AfterEach
    void limpiarSingleton() {
        // Evita que el estado del Singleton se filtre entre tests de otras clases
        GestorTipoCambio.getInstancia().limpiarTiposCambio();
    }

    @Test
    @DisplayName("crear COMPRA debe usar el Factory Method y guardar la transaccion")
    void crearCompraDebeUsarFactoryMethodYGuardar() {
        Transaccion resultado = transaccionService.crear(
                TipoOperacion.COMPRA, clienteNatural, parPenUsd, new BigDecimal("100"));

        assertEquals(TipoOperacion.COMPRA, resultado.getTipo());
        assertEquals(EstadoTransaccion.PENDIENTE, resultado.getEstado());
        verify(transaccionRepository).guardar(resultado);
    }

    @Test
    @DisplayName("crear debe usar el Singleton para obtener el tipo de cambio vigente y calcular el detalle")
    void crearDebeUsarSingletonYCalcularDetalle() {
        Transaccion resultado = transaccionService.crear(
                TipoOperacion.VENTA, clienteNatural, parPenUsd, new BigDecimal("100"));

        assertNotNull(resultado.getDetalle(), "El detalle debe calcularse usando el TipoCambio del Singleton");
        assertNotNull(resultado.getDetalle().getTipoCambioAplicado());
    }

    @Test
    @DisplayName("Decorator: cliente VIP debe pagar menos que un cliente NATURAL en VENTA")
    void clienteVipDebePagarMenosQueNatural() {
        Transaccion transaccionNatural = transaccionService.crear(
                TipoOperacion.VENTA, clienteNatural, parPenUsd, new BigDecimal("100"));
        Transaccion transaccionVip = transaccionService.crear(
                TipoOperacion.VENTA, clienteVip, parPenUsd, new BigDecimal("100"));

        BigDecimal precioNatural = transaccionNatural.getDetalle().getTipoCambioAplicado();
        BigDecimal precioVip = transaccionVip.getDetalle().getTipoCambioAplicado();

        assertTrue(precioVip.compareTo(precioNatural) < 0,
                "El precio de venta VIP (" + precioVip + ") debe ser menor al de un cliente NATURAL (" + precioNatural + ")");
    }

    @Test
    @DisplayName("Decorator: el precio final de venta debe llevar comision e impuesto sobre el precio base")
    void precioFinalVentaDebeIncluirComisionEImpuesto() {
        Transaccion resultado = transaccionService.crear(
                TipoOperacion.VENTA, clienteNatural, parPenUsd, new BigDecimal("100"));

        BigDecimal precioBase = new BigDecimal("3.800");
        BigDecimal precioFinal = resultado.getDetalle().getTipoCambioAplicado();

        assertTrue(precioFinal.compareTo(precioBase) > 0,
                "El precio final de venta debe ser mayor al precio base por la comision + impuesto aplicados");
    }

    @Test
    @DisplayName("crear TRANSFERENCIA no debe consultar tipo de cambio ni calcular detalle")
    void crearTransferenciaNoDebeCalcularDetalle() {
        Transaccion resultado = transaccionService.crear(
                TipoOperacion.TRANSFERENCIA, clienteNatural, parPenUsd, new BigDecimal("500"));

        assertEquals(TipoOperacion.TRANSFERENCIA, resultado.getTipo());
        assertNull(resultado.getDetalle());
    }

    @Test
    @DisplayName("crear sin tipo de cambio vigente no debe lanzar excepcion, solo advertencia por log")
    void crearSinTipoDeCambioVigenteNoDebeFallar() {
        GestorTipoCambio.getInstancia().limpiarTiposCambio();

        Transaccion resultado = assertDoesNotThrow(() -> transaccionService.crear(
                TipoOperacion.VENTA, clienteNatural, parPenUsd, new BigDecimal("100")));

        assertNull(resultado.getDetalle());
    }

    @Test
    @DisplayName("confirmar debe cambiar el estado a COMPLETADA y guardar")
    void confirmarDebeCambiarEstadoYGuardar() {
        Transaccion transaccion = new Transaccion();
        transaccion.setId(1L);
        transaccion.setEstado(EstadoTransaccion.PENDIENTE);
        when(transaccionRepository.buscarPorId(1L)).thenReturn(Optional.of(transaccion));

        Transaccion resultado = transaccionService.confirmar(1L);

        assertEquals(EstadoTransaccion.COMPLETADA, resultado.getEstado());
        verify(transaccionRepository).guardar(transaccion);
    }

    @Test
    @DisplayName("Observer: confirmar debe notificar a los observadores suscritos")
    void confirmarDebeNotificarAObservadores() {
        ObservadorEvento observadorEspia = mock(ObservadorEvento.class);
        PublicadorEventos publicador = new PublicadorEventos();
        publicador.suscribir(observadorEspia);
        TransaccionServiceImpl servicioConEspia = new TransaccionServiceImpl(transaccionRepository, publicador);

        Transaccion transaccion = new Transaccion();
        transaccion.setId(1L);
        transaccion.setEstado(EstadoTransaccion.PENDIENTE);
        when(transaccionRepository.buscarPorId(1L)).thenReturn(Optional.of(transaccion));

        servicioConEspia.confirmar(1L);

        verify(observadorEspia).actualizar(any(EventoSistema.class));
    }

    @Test
    @DisplayName("Observer: anular debe notificar a los observadores suscritos")
    void anularDebeNotificarAObservadores() {
        ObservadorEvento observadorEspia = mock(ObservadorEvento.class);
        PublicadorEventos publicador = new PublicadorEventos();
        publicador.suscribir(observadorEspia);
        TransaccionServiceImpl servicioConEspia = new TransaccionServiceImpl(transaccionRepository, publicador);

        Transaccion transaccion = new Transaccion();
        transaccion.setId(4L);
        transaccion.setEstado(EstadoTransaccion.PENDIENTE);
        when(transaccionRepository.buscarPorId(4L)).thenReturn(Optional.of(transaccion));

        servicioConEspia.anular(4L);

        verify(observadorEspia).actualizar(any(EventoSistema.class));
    }

    @Test
    @DisplayName("confirmar debe lanzar excepcion si la transaccion no existe")
    void confirmarDebeLanzarExcepcionSiNoExiste() {
        when(transaccionRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transaccionService.confirmar(99L));
    }

    @Test
    @DisplayName("anular debe cambiar el estado a ANULADA y guardar")
    void anularDebeCambiarEstadoYGuardar() {
        Transaccion transaccion = new Transaccion();
        transaccion.setId(2L);
        transaccion.setEstado(EstadoTransaccion.PENDIENTE);
        when(transaccionRepository.buscarPorId(2L)).thenReturn(Optional.of(transaccion));

        Transaccion resultado = transaccionService.anular(2L);

        assertEquals(EstadoTransaccion.ANULADA, resultado.getEstado());
    }

    @Test
    @DisplayName("buscarPorId debe delegar en el repositorio")
    void buscarPorIdDebeDelegarEnRepositorio() {
        Transaccion transaccion = new Transaccion();
        transaccion.setId(3L);
        when(transaccionRepository.buscarPorId(3L)).thenReturn(Optional.of(transaccion));

        Optional<Transaccion> resultado = transaccionService.buscarPorId(3L);

        assertTrue(resultado.isPresent());
    }

    @Test
    @DisplayName("listarTodas debe retornar la lista del repositorio")
    void listarTodasDebeRetornarListaDelRepositorio() {
        when(transaccionRepository.listarTodas()).thenReturn(List.of(new Transaccion()));

        List<Transaccion> resultado = transaccionService.listarTodas();

        assertEquals(1, resultado.size());
    }
}