package pe.edu.utp.casacambio.service.patron.observer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.casacambio.modelo.notificacion.EventoSistema;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Pruebas unitarias para {@link AuditoriaObserver}, ConcreteObserver del
 * patron Observer que genera un RegistroAuditoria ante cualquier
 * EventoSistema publicado, incluso cuando el evento no tiene un usuario
 * especifico asociado (evento generado por el propio sistema).
 */
@DisplayName("Tests de AuditoriaObserver (Observer - ConcreteObserver)")
class AuditoriaObserverTest {

    private final AuditoriaObserver observer = new AuditoriaObserver();

    @Test
    @DisplayName("actualizar debe registrar la auditoria sin lanzar excepcion")
    void actualizarDebeRegistrarAuditoriaSinFallar() {
        EventoSistema evento = new EventoSistema();
        evento.setNombre("TRANSACCION_COMPLETADA");
        evento.setDescripcion("Transaccion #1 - VENTA");
        evento.setOrigenClase("TransaccionServiceImpl");

        assertDoesNotThrow(() -> observer.actualizar(evento));
    }

    @Test
    @DisplayName("actualizar debe funcionar aunque el evento no tenga usuario asociado (evento del sistema)")
    void actualizarSinUsuarioAsociadoNoDebeFallar() {
        EventoSistema evento = new EventoSistema();
        evento.setNombre("TIPO_CAMBIO_ACTUALIZADO");
        evento.setOrigenClase("GestorTipoCambio");

        assertDoesNotThrow(() -> observer.actualizar(evento));
    }
}
