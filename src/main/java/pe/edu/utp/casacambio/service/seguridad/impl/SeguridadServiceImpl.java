package pe.edu.utp.casacambio.service.seguridad.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.edu.utp.casacambio.modelo.seguridad.PoliticaContrasena;
import pe.edu.utp.casacambio.modelo.seguridad.Sesion;
import pe.edu.utp.casacambio.modelo.seguridad.TokenSeguridad;
import pe.edu.utp.casacambio.modelo.tipocambio.Usuario;
import pe.edu.utp.casacambio.service.seguridad.SeguridadService;

/**
 * Implementacion de la capa de seguridad del sistema.
 *
 * Coordina la validacion de contrasenas segun una politica configurable,
 * la gestion del ciclo de vida de sesiones y la verificacion de tokens.
 *
 * Principio SOLID - SRP: unica responsabilidad es coordinar operaciones de seguridad.
 * Principio SOLID - DIP: depende de interfaces de dominio, no de infraestructura concreta.
 */
@Service
@Slf4j
public class SeguridadServiceImpl implements SeguridadService {

    /**
     * Valida si una contrasena cumple con la politica indicada.
     * Delega la logica de validacion al metodo de dominio de PoliticaContrasena,
     * preservando el principio Tell Don't Ask.
     *
     * @param contrasena contrasena a evaluar
     * @param politica   politica con las reglas a aplicar
     * @return true si la contrasena cumple todas las reglas
     */
    @Override
    public boolean validarContrasena(String contrasena, PoliticaContrasena politica) {
        boolean resultado = politica.validar(contrasena);
        log.info("Validacion de contrasena: {}", resultado ? "aprobada" : "rechazada");
        return resultado;
    }

    /**
     * Inicia una sesion para el usuario dado.
     * Crea un objeto Sesion, le asigna el usuario y delega el inicio
     * al metodo de dominio de la entidad.
     *
     * @param usuario usuario que inicia sesion
     * @return sesion creada y activa
     */
    @Override
    public Sesion iniciarSesion(Usuario usuario) {
        Sesion sesion = new Sesion();
        sesion.setUsuario(usuario);
        sesion.iniciar();
        log.info("Sesion iniciada para usuario: {}", usuario.getUsername());
        return sesion;
    }

    /**
     * Cierra la sesion indicada delegando al metodo de dominio de la entidad.
     *
     * @param sesion sesion a cerrar
     */
    @Override
    public void cerrarSesion(Sesion sesion) {
        sesion.cerrar();
        log.info("Sesion cerrada correctamente");
    }

    /**
     * Verifica si el token dado sigue siendo valido, usando el reloj
     * del sistema configurado en la zona horaria de Peru.
     *
     * @param token token a verificar
     * @return true si el token es vigente y no ha expirado
     */
    @Override
    public boolean verificarToken(TokenSeguridad token) {
        boolean valido = token.esValido();
        log.info("Verificacion de token: {}", valido ? "valido" : "invalido o expirado");
        return valido;
    }

    /**
     * Invalida el token dado, por ejemplo al momento de cerrar sesion.
     *
     * @param token token a invalidar
     */
    @Override
    public void invalidarToken(TokenSeguridad token) {
        token.invalidar();
        log.info("Token invalidado correctamente");
    }

    /**
     * Autentica un usuario verificando si la contrasena ingresada
     * coincide con la registrada en el sistema.
     *
     * @param usuario    usuario a autenticar
     * @param contrasena contrasena ingresada
     * @return true si la autenticacion es exitosa
     */
    @Override
    public boolean autenticarUsuario(Usuario usuario, String contrasena) {
        boolean autenticado = usuario.autenticar(contrasena);
        log.info("Autenticacion del usuario '{}': {}", usuario.getUsername(),
                autenticado ? "exitosa" : "fallida");
        return autenticado;
    }
}
