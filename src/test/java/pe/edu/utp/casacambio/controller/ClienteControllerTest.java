package pe.edu.utp.casacambio.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.utp.casacambio.dto.request.ClienteRequestDTO;
import pe.edu.utp.casacambio.dto.response.ClienteResponseDTO;
import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.cliente.TipoCliente;
import pe.edu.utp.casacambio.service.cliente.ClienteService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController controller;

    @Test
    void registrar_conTipoVip_debeRetornarCreatedYMapearDatos() {
        ClienteRequestDTO request = requestBase();
        request.setTipoCliente(TipoCliente.VIP);

        when(clienteService.registrar(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente cliente = invocation.getArgument(0);
            cliente.setId(10L);
            return cliente;
        });

        ResponseEntity<ClienteResponseDTO> response = controller.registrar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getId());
        assertEquals("Ana Torres", response.getBody().getNombre());
        assertEquals(TipoCliente.VIP, response.getBody().getTipoCliente());
        verify(clienteService).registrar(argThat(c -> c.getTipoCliente() == TipoCliente.VIP));
    }

    @Test
    void registrar_sinTipo_debeAsignarNatural() {
        ClienteRequestDTO request = requestBase();
        request.setTipoCliente(null);

        when(clienteService.registrar(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<ClienteResponseDTO> response = controller.registrar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TipoCliente.NATURAL, response.getBody().getTipoCliente());
        verify(clienteService).registrar(argThat(c -> c.getTipoCliente() == TipoCliente.NATURAL));
    }

    @Test
    void buscarPorId_existente_debeRetornarOk() {
        Cliente cliente = cliente(1L, "Luis", TipoCliente.NATURAL);
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        ResponseEntity<ClienteResponseDTO> response = controller.buscarPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Luis", response.getBody().getNombre());
    }

    @Test
    void buscarPorId_inexistente_debeRetornarNotFound() {
        when(clienteService.buscarPorId(99L)).thenReturn(Optional.empty());

        ResponseEntity<ClienteResponseDTO> response = controller.buscarPorId(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void listarTodos_debeMapearLista() {
        when(clienteService.listarTodos()).thenReturn(List.of(
                cliente(1L, "Uno", TipoCliente.NATURAL),
                cliente(2L, "Dos", TipoCliente.VIP)
        ));

        ResponseEntity<List<ClienteResponseDTO>> response = controller.listarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(TipoCliente.VIP, response.getBody().get(1).getTipoCliente());
    }

    private ClienteRequestDTO requestBase() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setNombre("Ana Torres");
        request.setDni("70123456");
        request.setEmail("ana@correo.com");
        request.setTelefono("999111222");
        return request;
    }

    private Cliente cliente(Long id, String nombre, TipoCliente tipo) {
        return new Cliente(id, nombre, "DNI" + id, nombre.toLowerCase() + "@correo.com", "90000000" + id, tipo);
    }
}
