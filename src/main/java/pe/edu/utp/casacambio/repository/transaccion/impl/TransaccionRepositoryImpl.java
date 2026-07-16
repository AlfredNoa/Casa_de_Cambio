package pe.edu.utp.casacambio.repository.transaccion.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;
import pe.edu.utp.casacambio.repository.transaccion.TransaccionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TransaccionRepositoryImpl implements TransaccionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Transaccion guardar(Transaccion transaccion) {
        if (transaccion.getId() == null) {
            entityManager.persist(transaccion);
            return transaccion;
        } else {
            return entityManager.merge(transaccion);
        }
    }

    @Override
    public Optional<Transaccion> buscarPorId(Long id) {
        return Optional.ofNullable(entityManager.find(Transaccion.class, id));
    }

    @Override
    public List<Transaccion> listarTodas() {
        return entityManager.createQuery("SELECT t FROM Transaccion t", Transaccion.class).getResultList();
    }

    @Override
    public void eliminar(Long id) {
        Transaccion transaccion = entityManager.find(Transaccion.class, id);
        if (transaccion != null) {
            entityManager.remove(transaccion);
        }
    }
}
