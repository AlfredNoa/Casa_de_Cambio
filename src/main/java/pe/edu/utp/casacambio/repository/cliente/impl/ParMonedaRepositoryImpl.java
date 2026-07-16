package pe.edu.utp.casacambio.repository.cliente.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import pe.edu.utp.casacambio.repository.cliente.ParMonedaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ParMonedaRepositoryImpl implements ParMonedaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ParMoneda guardar(ParMoneda parMoneda) {
        if (parMoneda.getId() == null) {
            entityManager.persist(parMoneda);
            return parMoneda;
        } else {
            return entityManager.merge(parMoneda);
        }
    }

    @Override
    public Optional<ParMoneda> buscarPorCodigos(String codigoOrigen, String codigoDestino) {
        List<ParMoneda> pares = entityManager.createQuery(
                        "SELECT p FROM ParMoneda p WHERE p.monedaOrigen.codigo = :origen "
                                + "AND p.monedaDestino.codigo = :destino", ParMoneda.class)
                .setParameter("origen", codigoOrigen)
                .setParameter("destino", codigoDestino)
                .getResultList();
        return pares.isEmpty() ? Optional.empty() : Optional.of(pares.get(0));
    }
}
