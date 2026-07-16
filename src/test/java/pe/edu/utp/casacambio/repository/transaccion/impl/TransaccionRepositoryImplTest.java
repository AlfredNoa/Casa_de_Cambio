package pe.edu.utp.casacambio.repository.transaccion.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionRepositoryImplTest {

    @Mock EntityManager entityManager;
    @Mock TypedQuery<Transaccion> query;
    @InjectMocks TransaccionRepositoryImpl repository;

    @Test
    void guardar_nueva_debePersistir() {
        Transaccion transaccion = new Transaccion();
        assertSame(transaccion, repository.guardar(transaccion));
        verify(entityManager).persist(transaccion);
    }

    @Test
    void guardar_existente_debeMerge() {
        Transaccion transaccion = new Transaccion();
        transaccion.setId(1L);
        Transaccion fusionada = new Transaccion();
        when(entityManager.merge(transaccion)).thenReturn(fusionada);

        assertSame(fusionada, repository.guardar(transaccion));
    }

    @Test
    void buscarPorId_existenteYNoExistente() {
        Transaccion transaccion = new Transaccion();
        when(entityManager.find(Transaccion.class, 1L)).thenReturn(transaccion);
        when(entityManager.find(Transaccion.class, 2L)).thenReturn(null);

        assertEquals(transaccion, repository.buscarPorId(1L).orElseThrow());
        assertTrue(repository.buscarPorId(2L).isEmpty());
    }

    @Test
    void listarTodas_debeRetornarConsulta() {
        List<Transaccion> lista = List.of(new Transaccion());
        when(entityManager.createQuery("SELECT t FROM Transaccion t", Transaccion.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(lista);

        assertSame(lista, repository.listarTodas());
    }

    @Test
    void eliminar_existente_debeRemover() {
        Transaccion transaccion = new Transaccion();
        when(entityManager.find(Transaccion.class, 3L)).thenReturn(transaccion);

        repository.eliminar(3L);

        verify(entityManager).remove(transaccion);
    }

    @Test
    void eliminar_inexistente_noDebeRemover() {
        when(entityManager.find(Transaccion.class, 4L)).thenReturn(null);

        repository.eliminar(4L);

        verify(entityManager, never()).remove(any());
    }
}
