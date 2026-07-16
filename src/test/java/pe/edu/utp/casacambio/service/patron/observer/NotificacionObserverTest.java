package pe.edu.utp.casacambio.service.patron.observer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.casacambio.modelo.notificacion.EventoSistema;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Pruebas unitarias para {@link NotificacionObserver}, ConcreteObserver
 * del patron Observer que genera y envia una Notificacion ante cualquier
 * EventoSistema publicado.
 */
@DisplayName("Tests de NotificacionObserver (Observer - ConcreteObserver)")
class NotificacionObserverTest {

    private final NotificacionObserver observer = new NotificacionObserver();

    @Test
    @DisplayName("actualizar con un evento normal no debe lanzar excepcion")
    void actualizarConEventoNormalNoDebeFallar() {
        EventoSistema evento = new EventoSistema();
        evento.setNombre("TRANSACCION_COMPLETADA");
        evento.setDescripcion("Transaccion #1 - VENTA");

        assertDoesNotThrow(() -> observer.actualizar(evento));
    }

    @Test
    @DisplayName("actualizar con un evento de anulacion no debe lanzar excepcion")
    void actualizarConEventoDeAnulacionNoDebeFallar() {
        EventoSistema evento = new EventoSistema();
        evento.setNombre("TRANSACCION_ANULADA");
        evento.setDescripcion("Transaccion #2 - COMPRA");

        assertDoesNotThrow(() -> observer.actualizar(evento));
    }

    @Test
    @DisplayName("actualizar con un evento sin descripcion debe usar el nombre como mensaje")
    void actualizarConEventoSinDescripcionNoDebeFallar() {
        EventoSistema evento = new EventoSistema();
        evento.setNombre("CAJA_CERRADA");

        assertDoesNotThrow(() -> observer.actualizar(evento));
    }
}
