package pe.edu.utp.casacambio.service.patron.factory;

import pe.edu.utp.casacambio.modelo.cliente.TipoOperacion;
import pe.edu.utp.casacambio.modelo.transaccion.EstadoTransaccion;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;
import lombok.extern.slf4j.Slf4j;

/**
 * Creator concreto para transacciones de tipo VENTA.
 * Patron: Factory Method. Principio SOLID: SRP.
 *
 * VENTA: la casa de cambio entrega moneda extranjera al cliente
 * y recibe soles a cambio.
 */
@Slf4j
public class VentaTransaccionCreator implements TransaccionCreator {

    /**
     * Crea una transaccion de VENTA con estado inicial PENDIENTE.
     *
     * @return transaccion preconfigurada como VENTA
     */
    @Override
    public Transaccion crearTransaccion() {
        // Creamos la transaccion y configuramos su tipo y estado inicial
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo(TipoOperacion.VENTA);
        transaccion.setEstado(EstadoTransaccion.PENDIENTE);
        log.info("Factory: transaccion de VENTA creada.");
        return transaccion;
    }
}
