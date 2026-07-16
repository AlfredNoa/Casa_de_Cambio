package pe.edu.utp.casacambio.repository.cliente.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.casacambio.modelo.cliente.Moneda;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonedaRepositoryImplTest {

    @Mock EntityManager entityManager;
    @Mock TypedQuery<Moneda> query;
    @InjectMocks MonedaRepositoryImpl repository;

    @Test
    void guardar_nueva_debePersistir() {
        Moneda moneda = new Moneda();
        assertSame(moneda, repository.guardar(moneda));
        verify(entityManager).persist(moneda);
    }

    @Test
    void guardar_existente_debeMerge() {
        Moneda moneda = new Moneda();
        moneda.setId(1L);
        Moneda fusionada = new Moneda();
        when(entityManager.merge(moneda)).thenReturn(fusionada);

        assertSame(fusionada, repository.guardar(moneda));
    }

    @Test
    void buscarPorCodigo_conResultado() {
        Moneda moneda = new Moneda();
        when(entityManager.createQuery("SELECT m FROM Moneda m WHERE m.codigo = :codigo", Moneda.class)).thenReturn(query);
        when(query.setParameter("codigo", "PEN")).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(moneda));

        assertEquals(moneda, repository.buscarPorCodigo("PEN").orElseThrow());
    }

    @Test
    void buscarPorCodigo_sinResultado() {
        when(entityManager.createQuery("SELECT m FROM Moneda m WHERE m.codigo = :codigo", Moneda.class)).thenReturn(query);
        when(query.setParameter("codigo", "ZZZ")).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        assertTrue(repository.buscarPorCodigo("ZZZ").isEmpty());
    }
}
