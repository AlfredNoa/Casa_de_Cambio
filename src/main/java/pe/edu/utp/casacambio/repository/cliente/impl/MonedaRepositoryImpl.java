package pe.edu.utp.casacambio.repository.cliente.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.casacambio.modelo.cliente.Moneda;
import pe.edu.utp.casacambio.repository.cliente.MonedaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class MonedaRepositoryImpl implements MonedaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Moneda guardar(Moneda moneda) {
        if (moneda.getId() == null) {
            entityManager.persist(moneda);
            return moneda;
        } else {
            return entityManager.merge(moneda);
        }
    }

    @Override
    public Optional<Moneda> buscarPorCodigo(String codigo) {
        List<Moneda> monedas = entityManager.createQuery(
                        "SELECT m FROM Moneda m WHERE m.codigo = :codigo", Moneda.class)
                .setParameter("codigo", codigo)
                .getResultList();
        return monedas.isEmpty() ? Optional.empty() : Optional.of(monedas.get(0));
    }
}
