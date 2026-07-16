package pe.edu.utp.casacambio.service.seguridad;

import pe.edu.utp.casacambio.modelo.seguridad.PoliticaContrasena;
import pe.edu.utp.casacambio.modelo.seguridad.Sesion;
import pe.edu.utp.casacambio.modelo.seguridad.TokenSeguridad;
import pe.edu.utp.casacambio.modelo.tipocambio.Usuario;

/**
 * Contrato de la capa de seguridad del sistema.
 * Centraliza la validacion de contrasenas, la gestion de sesiones
 * y la verificacion de tokens.
 *
 * Principio SOLID - ISP: expone solo las operaciones relevantes a seguridad.
 * Principio SOLID - DIP: las capas superiores dependen de esta interfaz,
 * no de una implementacion concreta.
 */
public interface SeguridadService {

    /**
     * Valida si una contrasena cumple con la politica indicada.
     *
     * @param contrasena  contrasena a evaluar
     * @param politica    politica con las reglas a aplicar
     * @return true si la contrasena cumple todas las reglas
     */
    boolean validarContrasena(String contrasena, PoliticaContrasena politica);

    /**
     * Inicia una sesion para el usuario dado.
     *
     * @param usuario usuario que inicia sesion
     * @return sesion creada y activa
     */
    Sesion iniciarSesion(Usuario usuario);

    /**
     * Cierra la sesion indicada.
     *
     * @param sesion sesion a cerrar
     */
    void cerrarSesion(Sesion sesion);

    /**
     * Verifica si el token dado sigue siendo valido.
     *
     * @param token token a verificar
     * @return true si el token es vigente y no ha expirado
     */
    boolean verificarToken(TokenSeguridad token);

    /**
     * Invalida el token dado, por ejemplo al cerrar sesion.
     *
     * @param token token a invalidar
     */
    void invalidarToken(TokenSeguridad token);

    /**
     * Autentica un usuario verificando su contrasena.
     *
     * @param usuario    usuario a autenticar
     * @param contrasena contrasena ingresada
     * @return true si la contrasena es correcta
     */
    boolean autenticarUsuario(Usuario usuario, String contrasena);
}
