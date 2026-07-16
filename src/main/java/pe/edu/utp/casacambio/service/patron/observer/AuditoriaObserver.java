package pe.edu.utp.casacambio.service.patron.observer;

import pe.edu.utp.casacambio.modelo.notificacion.EventoSistema;
import pe.edu.utp.casacambio.modelo.notificacion.RegistroAuditoria;

/**
 * ConcreteObserver: ante cualquier EventoSistema publicado, genera un
 * {@link RegistroAuditoria} para dejar trazabilidad de lo ocurrido.
 *
 * Patron: Observer.
 */
public class AuditoriaObserver implements ObservadorEvento {

    @Override
    public void actualizar(EventoSistema evento) {
        RegistroAuditoria registro = new RegistroAuditoria();
        registro.setAccion(evento.getNombre());
        registro.setEntidadAfectada(evento.getOrigenClase());
        registro.setDetalles(evento.getDescripcion());
        registro.guardar();
    }
}
