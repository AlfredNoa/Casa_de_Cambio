package pe.edu.utp.casacambio.repository.cliente.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.repository.cliente.ClienteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ClienteRepositoryImpl implements ClienteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Cliente guardar(Cliente cliente) {
        if (cliente.getId() == null) {
            entityManager.persist(cliente);
            return cliente;
        } else {
            return entityManager.merge(cliente);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return Optional.ofNullable(entityManager.find(Cliente.class, id));
    }

    @Override
    public Optional<Cliente> buscarPorDni(String dni) {
        List<Cliente> clientes = entityManager.createQuery("SELECT c FROM Cliente c WHERE c.dni = :dni", Cliente.class)
                .setParameter("dni", dni)
                .getResultList();
        return clientes.isEmpty() ? Optional.empty() : Optional.of(clientes.get(0));
    }

    @Override
    public List<Cliente> listarTodos() {
        return entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }
}
