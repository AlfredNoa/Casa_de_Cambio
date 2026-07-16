package pe.edu.utp.casacambio.service.cliente.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.casacambio.exception.ClienteYaExisteException;
import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.cliente.TipoCliente;
import pe.edu.utp.casacambio.repository.cliente.ClienteRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link ClienteServiceImpl}.
 *
 * Se prueba la logica de negocio a traves del service (no la entidad
 * Cliente de forma aislada), simulando ClienteRepository con Mockito.
 * Reemplaza a los antiguos ClienteTest y MonedaParMonedaTest, que
 * probaban clases de modelo directamente (SonarQube/indicacion del
 * docente: las pruebas unitarias deben estar solo en service/impl y patrones).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ClienteServiceImpl")
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    private ClienteServiceImpl clienteService;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        clienteService = new ClienteServiceImpl(clienteRepository);
        cliente = new Cliente(null, "Juan Pérez", "12345678",
                "juan@email.com", "987654321", TipoCliente.NATURAL);
    }

    @Test
    @DisplayName("registrar debe guardar el cliente cuando el DNI no existe")
    void registrarDebeGuardarClienteNuevo() {
        when(clienteRepository.buscarPorDni("12345678")).thenReturn(Optional.empty());
        when(clienteRepository.guardar(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.registrar(cliente);

        assertEquals("Juan Pérez", resultado.getNombre());
        verify(clienteRepository).guardar(cliente);
    }

    @Test
    @DisplayName("registrar debe lanzar ClienteYaExisteException si el DNI ya esta registrado")
    void registrarDebeLanzarExcepcionSiDniDuplicado() {
        when(clienteRepository.buscarPorDni("12345678")).thenReturn(Optional.of(cliente));

        assertThrows(ClienteYaExisteException.class, () -> clienteService.registrar(cliente));
        verify(clienteRepository, never()).guardar(any());
    }

    @Test
    @DisplayName("registrar debe conservar el tipo de cliente VIP")
    void registrarDebeConservarTipoClienteVip() {
        Cliente clienteVip = new Cliente(null, "María García", "87654321",
                "maria@email.com", "999888777", TipoCliente.VIP);
        when(clienteRepository.buscarPorDni("87654321")).thenReturn(Optional.empty());
        when(clienteRepository.guardar(clienteVip)).thenReturn(clienteVip);

        Cliente resultado = clienteService.registrar(clienteVip);

        assertEquals(TipoCliente.VIP, resultado.getTipoCliente());
    }

    @Test
    @DisplayName("buscarPorId debe delegar en el repositorio")
    void buscarPorIdDebeDelegarEnRepositorio() {
        when(clienteRepository.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombre());
    }

    @Test
    @DisplayName("listarTodos debe retornar la lista del repositorio")
    void listarTodosDebeRetornarListaDelRepositorio() {
        when(clienteRepository.listarTodos()).thenReturn(List.of(cliente));

        List<Cliente> resultado = clienteService.listarTodos();

        assertEquals(1, resultado.size());
    }
}
