package pe.edu.utp.casacambio.repository.cliente.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParMonedaRepositoryImplTest {

    private static final String JPQL = "SELECT p FROM ParMoneda p WHERE p.monedaOrigen.codigo = :origen "
            + "AND p.monedaDestino.codigo = :destino";

    @Mock EntityManager entityManager;
    @Mock TypedQuery<ParMoneda> query;
    @InjectMocks ParMonedaRepositoryImpl repository;

    @Test
    void guardar_nuevo_debePersistir() {
        ParMoneda par = new ParMoneda();
        assertSame(par, repository.guardar(par));
        verify(entityManager).persist(par);
    }

    @Test
    void guardar_existente_debeMerge() {
        ParMoneda par = new ParMoneda();
        par.setId(1L);
        ParMoneda fusionado = new ParMoneda();
        when(entityManager.merge(par)).thenReturn(fusionado);

        assertSame(fusionado, repository.guardar(par));
    }

    @Test
    void buscarPorCodigos_conResultado() {
        ParMoneda par = new ParMoneda();
        when(entityManager.createQuery(JPQL, ParMoneda.class)).thenReturn(query);
        when(query.setParameter("origen", "PEN")).thenReturn(query);
        when(query.setParameter("destino", "USD")).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(par));

        assertEquals(par, repository.buscarPorCodigos("PEN", "USD").orElseThrow());
    }

    @Test
    void buscarPorCodigos_sinResultado() {
        when(entityManager.createQuery(JPQL, ParMoneda.class)).thenReturn(query);
        when(query.setParameter("origen", "PEN")).thenReturn(query);
        when(query.setParameter("destino", "EUR")).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        assertTrue(repository.buscarPorCodigos("PEN", "EUR").isEmpty());
    }
}
