package pe.edu.utp.casacambio.service.patron.factory;

import pe.edu.utp.casacambio.modelo.cliente.TipoOperacion;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;

/**
 * Coordinador del Factory Method: selecciona el creator correcto
 * segun el tipo de operacion y delega la creacion.
 * Patron: Factory Method. Principio SOLID: OCP, DIP.
 */
public class TransaccionFactory {

    /** Constructor privado: clase utilitaria, no se instancia. */
    private TransaccionFactory() {}

    /**
     * Crea una transaccion del tipo indicado usando el creator correspondiente.
     * El codigo cliente no necesita conocer ningun creator concreto.
     *
     * @param tipo tipo de operacion deseada (COMPRA, VENTA, TRANSFERENCIA)
     * @return nueva transaccion preconfigurada segun el tipo
     */
    public static Transaccion crear(TipoOperacion tipo) {
        // Seleccionamos el creator adecuado segun el tipo de operacion
        TransaccionCreator creator = switch (tipo) {
            case COMPRA        -> new CompraTransaccionCreator();
            case VENTA         -> new VentaTransaccionCreator();
            case TRANSFERENCIA -> new TransferenciaTransaccionCreator();
        };
        // Delegamos la creacion al creator seleccionado
        return creator.crearTransaccion();
    }
}
