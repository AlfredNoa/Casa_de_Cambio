package pe.edu.utp.casacambio.service.cliente;

import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de logica de negocio para operaciones de Cliente.
 * Principio SOLID - ISP: interfaz especifica solo para clientes.
 * Principio SOLID - DIP: los controladores dependen de esta
 * interfaz, no de ClienteServiceImpl directamente.
 */
public interface ClienteService {

    /**
     * Registra un nuevo cliente verificando que el DNI no este duplicado.
     *
     * @param cliente datos del cliente a registrar
     * @return cliente registrado con su id asignado
     */
    Cliente registrar(Cliente cliente);

    /**
     * Busca un cliente por su identificador unico.
     *
     * @param id identificador del cliente
     * @return Optional con el cliente si existe, vacio si no
     */
    Optional<Cliente> buscarPorId(Long id);

    /**
     * Retorna todos los clientes registrados en el sistema.
     *
     * @return lista de clientes
     */
    List<Cliente> listarTodos();
}
