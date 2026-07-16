package pe.edu.utp.casacambio.service.seguridad.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.casacambio.modelo.seguridad.PoliticaContrasena;
import pe.edu.utp.casacambio.modelo.seguridad.Sesion;
import pe.edu.utp.casacambio.modelo.seguridad.TokenSeguridad;
import pe.edu.utp.casacambio.modelo.tipocambio.Usuario;
import pe.edu.utp.casacambio.service.seguridad.SeguridadService;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para {@link SeguridadServiceImpl}.
 *
 * Valida las operaciones de seguridad coordinadas por el servicio:
 * validacion de contrasenas, gestion de sesiones, verificacion
 * e invalidacion de tokens y autenticacion de usuarios.
 *
 * Los tests de expiracion de token usan fechas literales claramente en
 * el pasado/futuro lejano (no relativas a "ahora") para no depender del
 * reloj real del sistema (SonarQube java:S8692) ni del dia en que se
 * ejecuten las pruebas.
 */
@DisplayName("Tests de SeguridadServiceImpl")
class SeguridadServiceImplTest {

    /** Expiracion muy lejana en el futuro: el token siempre estara vigente. */
    private static final LocalDateTime EXPIRACION_LEJANA = LocalDateTime.of(2099, Month.DECEMBER, 31, 23, 59);

    /** Expiracion muy en el pasado: el token siempre estara expirado. */
    private static final LocalDateTime EXPIRACION_PASADA = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0);

    private SeguridadService seguridadService;
    private PoliticaContrasena politica;
    private Usuario usuario;
    private TokenSeguridad token;

    @BeforeEach
    void setUp() {
        seguridadService = new SeguridadServiceImpl();

        // Politica: min 8 chars, mayuscula, numero y caracter especial requeridos, 90 dias
        politica = new PoliticaContrasena(1L, 8, true, true, true, 90);

        usuario = new Usuario(1L, "cajero1", "clave123", "CAJERO", null);

        token = new TokenSeguridad(
                1L,
                "abc123token",
                usuario,
                EXPIRACION_LEJANA,
                true
        );
    }

    // -------------------------------------------------------------------------
    // Tests de validacion de contrasena
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Contraseña válida debe aprobarse según la política")
    void contraseniaValidaDebeAprobarse() {
        assertTrue(seguridadService.validarContrasena("Segura@1", politica));
    }

    @Test
    @DisplayName("Contraseña nula debe rechazarse")
    void contraseniaNulaDebeRechazarse() {
        assertFalse(seguridadService.validarContrasena(null, politica));
    }

    @Test
    @DisplayName("Contraseña demasiado corta debe rechazarse")
    void contraseniaCorataDebeRechazarse() {
        assertFalse(seguridadService.validarContrasena("Ab@1", politica));
    }

    @Test
    @DisplayName("Contraseña sin mayúscula debe rechazarse")
    void contraseniaSinMayusculaDebeRechazarse() {
        assertFalse(seguridadService.validarContrasena("segura@1", politica));
    }

    @Test
    @DisplayName("Contraseña sin número debe rechazarse")
    void contraseniaSinNumeroDebeRechazarse() {
        assertFalse(seguridadService.validarContrasena("Segura@A", politica));
    }

    @Test
    @DisplayName("Contraseña sin carácter especial debe rechazarse")
    void contraseniaSinCaracterEspecialDebeRechazarse() {
        assertFalse(seguridadService.validarContrasena("Segura12", politica));
    }

    // -------------------------------------------------------------------------
    // Tests de sesion
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Iniciar sesión debe retornar una sesión activa")
    void iniciarSesionDebeRetornarSesionActiva() {
        Sesion sesion = seguridadService.iniciarSesion(usuario);
        assertTrue(sesion.isActiva());
        assertNotNull(sesion.getHoraInicio());
        assertNull(sesion.getHoraFin());
    }

    @Test
    @DisplayName("Cerrar sesión debe marcarla como inactiva")
    void cerrarSesionDebeMarcarlaInactiva() {
        Sesion sesion = seguridadService.iniciarSesion(usuario);
        seguridadService.cerrarSesion(sesion);
        assertFalse(sesion.isActiva());
        assertNotNull(sesion.getHoraFin());
    }

    // -------------------------------------------------------------------------
    // Tests de token
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Token vigente y no expirado debe ser válido")
    void tokenVigenteDebeSerValido() {
        assertTrue(seguridadService.verificarToken(token));
    }

    @Test
    @DisplayName("Token expirado no debe ser válido")
    void tokenExpiradoNoDebeSerValido() {
        token.setFechaExpiracion(EXPIRACION_PASADA);
        assertFalse(seguridadService.verificarToken(token));
    }

    @Test
    @DisplayName("Token invalidado no debe ser válido")
    void tokenInvalidadoNoDebeSerValido() {
        seguridadService.invalidarToken(token);
        assertFalse(seguridadService.verificarToken(token));
        assertFalse(token.isVigente());
    }

    // -------------------------------------------------------------------------
    // Tests de autenticacion de usuario
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Autenticar con contraseña correcta debe retornar true")
    void autenticarConPasswordCorrectoDebeSerTrue() {
        assertTrue(seguridadService.autenticarUsuario(usuario, "clave123"));
    }

    @Test
    @DisplayName("Autenticar con contraseña incorrecta debe retornar false")
    void autenticarConPasswordIncorrectoDebeSerFalse() {
        assertFalse(seguridadService.autenticarUsuario(usuario, "incorrecta"));
    }
}
