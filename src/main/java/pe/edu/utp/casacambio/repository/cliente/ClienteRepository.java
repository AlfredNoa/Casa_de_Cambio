package pe.edu.utp.casacambio.repository.cliente;

import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de acceso a datos para la entidad Cliente.
 * Principio SOLID - DIP: las capas superiores dependen de esta
 * interfaz, no de una implementacion concreta de base de datos.
 */
public interface ClienteRepository {

    /**
     * Guarda o actualiza un cliente en el repositorio.
     *
     * @param cliente cliente a persistir
     * @return cliente guardado con su id asignado
     */
    Cliente guardar(Cliente cliente);

    /**
     * Busca un cliente por su identificador unico.
     *
     * @param id identificador del cliente
     * @return Optional con el cliente si existe, vacio si no
     */
    Optional<Cliente> buscarPorId(Long id);

    /**
     * Busca un cliente por su numero de documento de identidad.
     *
     * @param dni numero de documento
     * @return Optional con el cliente si existe, vacio si no
     */
    Optional<Cliente> buscarPorDni(String dni);

    /**
     * Retorna todos los clientes registrados en el sistema.
     *
     * @return lista de clientes, vacia si no hay ninguno
     */
    List<Cliente> listarTodos();
}
