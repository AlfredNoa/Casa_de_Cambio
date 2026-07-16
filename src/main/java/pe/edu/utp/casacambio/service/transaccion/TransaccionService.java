package pe.edu.utp.casacambio.service.transaccion;

import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import pe.edu.utp.casacambio.modelo.cliente.TipoOperacion;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de logica de negocio para operaciones de Transaccion.
 * Principio SOLID - ISP: interfaz especifica solo para transacciones.
 * Principio SOLID - DIP: los controladores dependen de esta
 * interfaz, no de TransaccionServiceImpl directamente.
 */
public interface TransaccionService {

    /**
     * Crea una nueva transaccion usando el Factory Method segun el tipo
     * indicado. Si es COMPRA o VENTA y existe tipo de cambio vigente
     * (Singleton GestorTipoCambio), calcula el detalle economico aplicando
     * el patron Decorator (comision, descuento VIP e impuesto).
     *
     * @param tipo tipo de operacion: COMPRA, VENTA o TRANSFERENCIA
     * @param cliente cliente que realiza la operacion
     * @param parMoneda par de monedas involucrado (ej: PEN/USD)
     * @param montoEntregado monto que el cliente entrega en la operacion
     * @return transaccion creada y guardada en el repositorio
     */
    Transaccion crear(TipoOperacion tipo, Cliente cliente, ParMoneda parMoneda, BigDecimal montoEntregado);

    /**
     * Confirma una transaccion cambiando su estado a COMPLETADA.
     *
     * @param id identificador de la transaccion a confirmar
     * @return transaccion confirmada
     */
    Transaccion confirmar(Long id);

    /**
     * Anula una transaccion cambiando su estado a ANULADA.
     *
     * @param id identificador de la transaccion a anular
     * @return transaccion anulada
     */
    Transaccion anular(Long id);

    /**
     * Busca una transaccion por su identificador unico.
     *
     * @param id identificador de la transaccion
     * @return Optional con la transaccion si existe, vacio si no
     */
    Optional<Transaccion> buscarPorId(Long id);

    /**
     * Retorna todas las transacciones registradas en el sistema.
     *
     * @return lista de transacciones
     */
    List<Transaccion> listarTodas();
}
