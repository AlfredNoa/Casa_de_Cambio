package pe.edu.utp.casacambio.service.patron.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.casacambio.modelo.notificacion.EventoSistema;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link PublicadorEventos}, el Subject del
 * patron Observer. Verifican que el mecanismo de suscripcion/notificacion
 * funcione correctamente de forma aislada (con observadores simulados),
 * independiente de que NotificacionObserver o AuditoriaObserver esten
 * bien implementados.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de PublicadorEventos (Observer - Subject)")
class PublicadorEventosTest {

    @Mock
    private ObservadorEvento observadorA;

    @Mock
    private ObservadorEvento observadorB;

    private PublicadorEventos publicador;
    private EventoSistema evento;

    @BeforeEach
    void setUp() {
        publicador = new PublicadorEventos();
        evento = new EventoSistema();
        evento.setNombre("TIPO_CAMBIO_ACTUALIZADO");
        evento.setOrigenClase("GestorTipoCambio");
    }

    @Test
    @DisplayName("suscribir debe aumentar la cantidad de observadores")
    void suscribirDebeAumentarCantidadDeObservadores() {
        publicador.suscribir(observadorA);
        publicador.suscribir(observadorB);

        assertEquals(2, publicador.cantidadObservadores());
    }

    @Test
    @DisplayName("publicar debe notificar a todos los observadores suscritos")
    void publicarDebeNotificarATodosLosObservadores() {
        publicador.suscribir(observadorA);
        publicador.suscribir(observadorB);

        publicador.publicar(evento);

        verify(observadorA).actualizar(evento);
        verify(observadorB).actualizar(evento);
    }

    @Test
    @DisplayName("publicar debe asignar fecha y hora al evento")
    void publicarDebeAsignarFechaAlEvento() {
        assertNull(evento.getFechaHora());

        publicador.publicar(evento);

        assertNotNull(evento.getFechaHora());
    }

    @Test
    @DisplayName("desuscribir debe evitar que el observador reciba notificaciones")
    void desuscribirDebeEvitarNotificaciones() {
        publicador.suscribir(observadorA);
        publicador.desuscribir(observadorA);

        publicador.publicar(evento);

        verify(observadorA, never()).actualizar(any());
    }

    @Test
    @DisplayName("publicar sin observadores suscritos no debe lanzar excepcion")
    void publicarSinObservadoresNoDebeFallar() {
        assertDoesNotThrow(() -> publicador.publicar(evento));
    }
}
