package pe.edu.utp.casacambio.modelo.transaccion;

/**
 * Estados posibles de una transaccion a lo largo de su ciclo de vida.
 *
 * PENDIENTE: recien creada, aun no procesada.
 * EN_PROCESO: en curso, esperando confirmacion.
 * COMPLETADA: finalizada exitosamente.
 * ANULADA: cancelada por el cajero o el sistema.
 */
public enum EstadoTransaccion {
    PENDIENTE,
    EN_PROCESO,
    COMPLETADA,
    ANULADA
}
