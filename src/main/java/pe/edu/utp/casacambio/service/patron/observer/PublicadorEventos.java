package pe.edu.utp.casacambio.service.patron.observer;

import pe.edu.utp.casacambio.modelo.notificacion.EventoSistema;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject del patron Observer: mantiene la lista de observadores
 * suscritos y les notifica cada vez que se publica un EventoSistema.
 *
 * Patron: Observer (de comportamiento).
 * Principio SOLID - SRP: su unica responsabilidad es gestionar la
 * suscripcion y notificacion de observadores, no que hacen con el evento.
 */
@Slf4j
public class PublicadorEventos {

    /** Observadores suscritos actualmente a este publicador. */
    private final List<ObservadorEvento> observadores = new ArrayList<>();

    /**
     * Suscribe un observador para que reciba los eventos publicados.
     *
     * @param observador observador a suscribir
     */
    public void suscribir(ObservadorEvento observador) {
        observadores.add(observador);
    }

    /**
     * Da de baja un observador; deja de recibir eventos publicados.
     *
     * @param observador observador a remover
     */
    public void desuscribir(ObservadorEvento observador) {
        observadores.remove(observador);
    }

    /**
     * Publica el evento (le asigna fecha/hora) y notifica a todos los
     * observadores suscritos, en el orden en que se suscribieron.
     *
     * @param evento evento del sistema a publicar
     */
    public void publicar(EventoSistema evento) {
        evento.publicar();
        log.info("Publicando evento '{}' a {} observador(es)", evento.getNombre(), observadores.size());
        for (ObservadorEvento observador : observadores) {
            observador.actualizar(evento);
        }
    }

    /**
     * Cantidad de observadores suscritos actualmente. Util para pruebas
     * y para verificar la configuracion del publicador.
     *
     * @return numero de observadores suscritos
     */
    public int cantidadObservadores() {
        return observadores.size();
    }
}
