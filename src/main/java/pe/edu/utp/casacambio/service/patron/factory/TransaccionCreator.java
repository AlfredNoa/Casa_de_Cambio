package pe.edu.utp.casacambio.service.patron.factory;

import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;

/**
 * Interfaz del Factory Method para la creacion de transacciones.
 * Patron: Factory Method. Principio SOLID: OCP, DIP.
 *
 * Cada implementacion decide como construir su tipo especifico
 * de transaccion sin que el codigo cliente lo sepa.
 */
public interface TransaccionCreator {

    /**
     * Crea y retorna una nueva instancia de Transaccion preconfigurada
     * segun el tipo de operacion que representa esta implementacion.
     *
     * @return nueva transaccion lista para operar
     */
    Transaccion crearTransaccion();
}
