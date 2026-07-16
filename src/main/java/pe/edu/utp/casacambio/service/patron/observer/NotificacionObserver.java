package pe.edu.utp.casacambio.service.patron.observer;

import pe.edu.utp.casacambio.modelo.notificacion.CanalNotificacion;
import pe.edu.utp.casacambio.modelo.notificacion.EventoSistema;
import pe.edu.utp.casacambio.modelo.notificacion.Notificacion;
import pe.edu.utp.casacambio.modelo.notificacion.TipoNotificacion;

/**
 * ConcreteObserver: ante cualquier EventoSistema publicado, genera y
 * envia una {@link Notificacion} para informar al usuario en pantalla.
 *
 * Patron: Observer.
 */
public class NotificacionObserver implements ObservadorEvento {

    @Override
    public void actualizar(EventoSistema evento) {
        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(evento.getDescripcion() != null
                ? evento.getDescripcion()
                : evento.getNombre());
        notificacion.setTipo(determinarTipo(evento));
        notificacion.setCanal(CanalNotificacion.PANTALLA);
        notificacion.enviar();
    }

    /**
     * Clasifica la notificacion segun el nombre del evento: si contiene
     * "ERROR" o "ANULAD" se marca como ALERTA, en caso contrario es
     * simplemente INFORMATIVA.
     */
    private TipoNotificacion determinarTipo(EventoSistema evento) {
        String nombre = evento.getNombre() != null ? evento.getNombre() : "";
        if (nombre.contains("ERROR") || nombre.contains("ANULAD")) {
            return TipoNotificacion.ALERTA;
        }
        return TipoNotificacion.INFORMATIVA;
    }
}
