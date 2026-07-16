package pe.edu.utp.casacambio.repository.cliente.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.casacambio.modelo.cliente.Cliente;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteRepositoryImplTest {

    @Mock EntityManager entityManager;
    @Mock TypedQuery<Cliente> query;
    @InjectMocks ClienteRepositoryImpl repository;

    @Test
    void guardar_nuevo_debePersistir() {
        Cliente cliente = new Cliente();

        Cliente resultado = repository.guardar(cliente);

        verify(entityManager).persist(cliente);
        assertSame(cliente, resultado);
    }

    @Test
    void guardar_existente_debeHacerMerge() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        Cliente fusionado = new Cliente();
        when(entityManager.merge(cliente)).thenReturn(fusionado);

        Cliente resultado = repository.guardar(cliente);

        assertSame(fusionado, resultado);
    }

    @Test
    void buscarPorId_existenteYNoExistente() {
        Cliente cliente = new Cliente();
        when(entityManager.find(Cliente.class, 1L)).thenReturn(cliente);
        when(entityManager.find(Cliente.class, 2L)).thenReturn(null);

        assertEquals(Optional.of(cliente), repository.buscarPorId(1L));
        assertTrue(repository.buscarPorId(2L).isEmpty());
    }

    @Test
    void buscarPorDni_conResultado_debeRetornarPrimero() {
        Cliente cliente = new Cliente();
        when(entityManager.createQuery("SELECT c FROM Cliente c WHERE c.dni = :dni", Cliente.class)).thenReturn(query);
        when(query.setParameter("dni", "70000001")).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(cliente));

        Optional<Cliente> resultado = repository.buscarPorDni("70000001");

        assertEquals(Optional.of(cliente), resultado);
    }

    @Test
    void buscarPorDni_sinResultados_debeRetornarVacio() {
        when(entityManager.createQuery("SELECT c FROM Cliente c WHERE c.dni = :dni", Cliente.class)).thenReturn(query);
        when(query.setParameter("dni", "x")).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        assertTrue(repository.buscarPorDni("x").isEmpty());
    }

    @Test
    void listarTodos_debeRetornarConsulta() {
        List<Cliente> clientes = List.of(new Cliente(), new Cliente());
        when(entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(clientes);

        assertSame(clientes, repository.listarTodos());
    }
}
