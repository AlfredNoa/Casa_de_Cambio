package pe.edu.utp.casacambio.exception;

/**
 * Se lanza cuando se intenta registrar un cliente con un DNI que
 * ya pertenece a otro cliente registrado en el sistema.
 *
 * Reemplaza el uso de RuntimeException generica (SonarQube java:S112),
 * permitiendo a los consumidores de ClienteService capturar este caso
 * de negocio especifico sin atrapar cualquier error inesperado.
 */
public class ClienteYaExisteException extends RuntimeException {

    public ClienteYaExisteException(String mensaje) {
        super(mensaje);
    }
}
