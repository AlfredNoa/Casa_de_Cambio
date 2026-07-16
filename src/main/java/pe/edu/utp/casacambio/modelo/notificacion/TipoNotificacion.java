package pe.edu.utp.casacambio.modelo.notificacion;

/**
 * Clasificacion de las notificaciones generadas por el sistema.
 *
 * ALERTA: requiere atencion inmediata del usuario.
 * INFORMATIVA: solo informacion, sin accion requerida.
 * ERROR: fallo en una operacion del sistema.
 */
public enum TipoNotificacion {
    ALERTA,
    INFORMATIVA,
    ERROR
}
