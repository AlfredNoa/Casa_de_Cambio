package pe.edu.utp.casacambio.service.patron.observer;

import pe.edu.utp.casacambio.modelo.notificacion.EventoSistema;

/**
 * Observer del patron Observer: cualquier clase que deba reaccionar a un
 * EventoSistema implementa esta interfaz y se suscribe a un
 * {@link PublicadorEventos}.
 *
 * Patron: Observer (de comportamiento).
 * Principio SOLID - OCP: se pueden agregar nuevos tipos de reaccion a un
 * evento (nuevos observadores) sin modificar quien publica el evento.
 */
public interface ObservadorEvento {

    /**
     * Metodo invocado por el Subject (PublicadorEventos) cuando se publica
     * un nuevo evento al que este observador esta suscrito.
     *
     * @param evento evento del sistema que acaba de ocurrir
     */
    void actualizar(EventoSistema evento);
}
