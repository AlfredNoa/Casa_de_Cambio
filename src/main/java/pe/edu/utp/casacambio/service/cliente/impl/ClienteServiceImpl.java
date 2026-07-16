package pe.edu.utp.casacambio.service.cliente.impl;

import pe.edu.utp.casacambio.exception.ClienteYaExisteException;
import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.repository.cliente.ClienteRepository;
import pe.edu.utp.casacambio.service.cliente.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Implementacion de ClienteService.
 * Principio SOLID - SRP: solo coordina la logica de negocio de clientes.
 * Principio SOLID - DIP: depende de la interfaz ClienteRepository, no de una clase concreta.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteServiceImpl implements ClienteService {

    /** Repositorio inyectado por constructor via @RequiredArgsConstructor. */
    private final ClienteRepository clienteRepository;

    /**
     * Registra un nuevo cliente verificando que el DNI no este duplicado.
     *
     * @param cliente datos del cliente a registrar
     * @return cliente registrado
     */
    @Override
    public Cliente registrar(Cliente cliente) {
        // Verificamos que no exista un cliente con el mismo DNI
        Optional<Cliente> existente = clienteRepository.buscarPorDni(cliente.getDni());
        if (existente.isPresent()) {
            throw new ClienteYaExisteException("Ya existe un cliente con DNI: " + cliente.getDni());
        }
        // Llamamos al metodo de dominio para registrar el cliente
        cliente.registrar();
        // Persistimos el cliente en el repositorio
        return clienteRepository.guardar(cliente);
    }

    /**
     * Busca un cliente por su identificador unico.
     *
     * @param id identificador del cliente
     * @return Optional con el cliente si existe
     */
    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.buscarPorId(id);
    }

    /**
     * Retorna todos los clientes registrados en el sistema.
     *
     * @return lista de clientes
     */
    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.listarTodos();
    }
}
