package pe.edu.utp.casacambio.service.patron.factory;

import pe.edu.utp.casacambio.modelo.cliente.TipoOperacion;
import pe.edu.utp.casacambio.modelo.transaccion.EstadoTransaccion;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;
import lombok.extern.slf4j.Slf4j;

/**
 * Creator concreto para transacciones de tipo COMPRA.
 * Patron: Factory Method. Principio SOLID: SRP.
 *
 * COMPRA: el cliente entrega moneda extranjera a la casa de cambio
 * y recibe soles a cambio.
 */
@Slf4j
public class CompraTransaccionCreator implements TransaccionCreator {

    /**
     * Crea una transaccion de COMPRA con estado inicial PENDIENTE.
     *
     * @return transaccion preconfigurada como COMPRA
     */
    @Override
    public Transaccion crearTransaccion() {
        // Creamos la transaccion y configuramos su tipo y estado inicial
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo(TipoOperacion.COMPRA);
        transaccion.setEstado(EstadoTransaccion.PENDIENTE);
        log.info("Factory: transaccion de COMPRA creada.");
        return transaccion;
    }
}
