package pe.edu.utp.casacambio.modelo.notificacion;

/**
 * Canal por el que se enviara una notificacion al usuario o al sistema.
 *
 * PANTALLA: se muestra en la interfaz del sistema en tiempo real.
 * EMAIL: se envia al correo electronico del usuario.
 * LOG: se registra en el archivo de log del servidor.
 */
public enum CanalNotificacion {
    PANTALLA,
    EMAIL,
    LOG
}
