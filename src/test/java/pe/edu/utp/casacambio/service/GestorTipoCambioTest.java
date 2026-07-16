package pe.edu.utp.casacambio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.casacambio.modelo.cliente.Moneda;
import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;
import pe.edu.utp.casacambio.service.patron.singleton.GestorTipoCambio;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

// Se usa un Clock fijo construido a partir de un instante LITERAL
// (sin llamar a Instant.now() ni a ningun otro metodo del reloj real)
// para que las pruebas sean completamente deterministicas y no dependan
// del dia en que se ejecuten (SonarQube java:S8692).

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el patrón Singleton {@link GestorTipoCambio}.
 *
 * Incluye la verificacion de vigencia por fecha (antes cubierta por un
 * TipoCambioTest de modelo/, ahora movida aqui porque es el Singleton
 * quien realmente aplica esa regla al recuperar un tipo de cambio).
 * Se usan las sobrecargas obtenerTipoCambio(ParMoneda, Clock) /
 * existeTipoCambio(ParMoneda, Clock) para no depender del reloj real,
 * ni siquiera indirectamente a traves del Singleton.
 */
@DisplayName("Tests de GestorTipoCambio (Singleton)")
class GestorTipoCambioTest {

    private static final Clock CLOCK_FIJO =
            Clock.fixed(Instant.parse("2026-07-13T17:00:00Z"), ZoneId.of("America/Lima"));
    private static final LocalDateTime FECHA_PRUEBA = LocalDateTime.now(CLOCK_FIJO);

    private GestorTipoCambio gestor;
    private ParMoneda parPenUsd;
    private TipoCambio tipoCambio;

    @BeforeEach
    void setUp() {
        gestor = GestorTipoCambio.getInstancia();
        gestor.limpiarTiposCambio();

        Moneda pen = new Moneda(1L, "PEN", "Sol peruano", "S/");
        Moneda usd = new Moneda(2L, "USD", "Dólar americano", "$");
        parPenUsd = new ParMoneda(1L, pen, usd, true);

        tipoCambio = new TipoCambio(
                1L, parPenUsd,
                new BigDecimal("3.70"),
                new BigDecimal("3.80"),
                FECHA_PRUEBA
        );
    }

    @Test
    @DisplayName("Debe retornar siempre la misma instancia (Singleton)")
    void debeRetornarMismaInstancia() {
        GestorTipoCambio instancia1 = GestorTipoCambio.getInstancia();
        GestorTipoCambio instancia2 = GestorTipoCambio.getInstancia();
        assertSame(instancia1, instancia2);
    }

    @Test
    @DisplayName("La instancia no debe ser nula")
    void instanciaNoDebeSerNula() {
        assertNotNull(gestor);
    }

    @Test
    @DisplayName("Debe actualizar y recuperar el tipo de cambio correctamente")
    void debeActualizarYRecuperarTipoCambio() {
        gestor.actualizarTipoCambio(tipoCambio);
        TipoCambio recuperado = gestor.obtenerTipoCambio(parPenUsd, CLOCK_FIJO);
        assertNotNull(recuperado);
        assertEquals(new BigDecimal("3.70"), recuperado.getPrecioCompra());
    }

    @Test
    @DisplayName("Debe confirmar existencia de tipo de cambio registrado")
    void debeConfirmarExistencia() {
        gestor.actualizarTipoCambio(tipoCambio);
        assertTrue(gestor.existeTipoCambio(parPenUsd, CLOCK_FIJO));
    }

    @Test
    @DisplayName("No debe existir tipo de cambio para par no registrado")
    void noDebeExistirParNoRegistrado() {
        Moneda pen = new Moneda(1L, "PEN", "Sol peruano", "S/");
        Moneda eur = new Moneda(3L, "EUR", "Euro", "€");
        ParMoneda parPenEur = new ParMoneda(2L, pen, eur, true);
        assertFalse(gestor.existeTipoCambio(parPenEur, CLOCK_FIJO));
    }

    @Test
    @DisplayName("Limpiar debe eliminar todos los tipos de cambio")
    void limpiarDebeEliminarTodos() {
        gestor.actualizarTipoCambio(tipoCambio);
        gestor.limpiarTiposCambio();
        assertFalse(gestor.existeTipoCambio(parPenUsd, CLOCK_FIJO));
    }

    @Test
    @DisplayName("No debe retornar un tipo de cambio registrado ayer (desactualizado)")
    void noDebeRetornarTipoCambioDeAyer() {
        tipoCambio.setFechaHora(FECHA_PRUEBA.minusDays(1));
        gestor.actualizarTipoCambio(tipoCambio);

        assertNull(gestor.obtenerTipoCambio(parPenUsd, CLOCK_FIJO));
        assertFalse(gestor.existeTipoCambio(parPenUsd, CLOCK_FIJO));
    }
}
