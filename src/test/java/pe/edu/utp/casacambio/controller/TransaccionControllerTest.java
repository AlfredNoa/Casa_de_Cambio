package pe.edu.utp.casacambio.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.utp.casacambio.dto.request.TransaccionRequestDTO;
import pe.edu.utp.casacambio.dto.response.TransaccionResponseDTO;
import pe.edu.utp.casacambio.modelo.cliente.*;
import pe.edu.utp.casacambio.modelo.transaccion.DetalleTransaccion;
import pe.edu.utp.casacambio.modelo.transaccion.EstadoTransaccion;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;
import pe.edu.utp.casacambio.service.cliente.ClienteService;
import pe.edu.utp.casacambio.service.transaccion.TransaccionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionControllerTest {

    @Mock
    private TransaccionService transaccionService;
    @Mock
    private ClienteService clienteService;
    @Mock
    private ParMonedaResolver parMonedaResolver;
    @InjectMocks
    private TransaccionController controller;

    @Test
    void crear_conDetalle_debeRetornarCreatedYMapearCampos() {
        Cliente cliente = cliente();
        ParMoneda par = par();
        Transaccion transaccion = transaccion(15L, EstadoTransaccion.PENDIENTE, true);
        TransaccionRequestDTO request = request();

        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(parMonedaResolver.resolver("PEN", "USD")).thenReturn(par);
        when(transaccionService.crear(TipoOperacion.VENTA, cliente, par, new BigDecimal("1000.00")))
                .thenReturn(transaccion);

        ResponseEntity<TransaccionResponseDTO> response = controller.crear(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(15L, response.getBody().getId());
        assertEquals("VENTA", response.getBody().getTipoOperacion());
        assertEquals("PEN/USD", response.getBody().getParMoneda());
        assertEquals(new BigDecimal("3800.00"), response.getBody().getMontoRecibido());
        assertEquals(new BigDecimal("3.80"), response.getBody().getTipoCambioUsado());
        assertEquals(new BigDecimal("10.00"), response.getBody().getComision());
    }

    @Test
    void crear_clienteInexistente_debeLanzarExcepcion() {
        TransaccionRequestDTO request = request();
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> controller.crear(request));

        assertEquals("Cliente no encontrado", ex.getMessage());
        verifyNoInteractions(parMonedaResolver, transaccionService);
    }

    @Test
    void confirmar_debeRetornarCompletada() {
        when(transaccionService.confirmar(5L)).thenReturn(transaccion(5L, EstadoTransaccion.COMPLETADA, true));

        ResponseEntity<TransaccionResponseDTO> response = controller.confirmar(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EstadoTransaccion.COMPLETADA, response.getBody().getEstado());
    }

    @Test
    void anular_debeRetornarAnulada() {
        when(transaccionService.anular(6L)).thenReturn(transaccion(6L, EstadoTransaccion.ANULADA, false));

        ResponseEntity<TransaccionResponseDTO> response = controller.anular(6L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(EstadoTransaccion.ANULADA, response.getBody().getEstado());
        assertNull(response.getBody().getMontoRecibido());
    }

    @Test
    void buscarPorId_existente_debeRetornarOk() {
        when(transaccionService.buscarPorId(7L)).thenReturn(Optional.of(transaccion(7L, EstadoTransaccion.PENDIENTE, false)));

        ResponseEntity<TransaccionResponseDTO> response = controller.buscarPorId(7L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(7L, response.getBody().getId());
    }

    @Test
    void buscarPorId_inexistente_debeRetornarNotFound() {
        when(transaccionService.buscarPorId(88L)).thenReturn(Optional.empty());

        ResponseEntity<TransaccionResponseDTO> response = controller.buscarPorId(88L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void listarTodas_debeMapearConYSinDetalle() {
        when(transaccionService.listarTodas()).thenReturn(List.of(
                transaccion(1L, EstadoTransaccion.PENDIENTE, true),
                transaccion(2L, EstadoTransaccion.PENDIENTE, false)
        ));

        ResponseEntity<List<TransaccionResponseDTO>> response = controller.listarTodas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertNotNull(response.getBody().get(0).getMontoRecibido());
        assertNull(response.getBody().get(1).getMontoRecibido());
    }

    @Test
    void listar_conParNulo_debeMapearParComoNull() {
        Transaccion transaccion = transaccion(3L, EstadoTransaccion.PENDIENTE, false);
        transaccion.setParMoneda(null);
        when(transaccionService.listarTodas()).thenReturn(List.of(transaccion));

        ResponseEntity<List<TransaccionResponseDTO>> response = controller.listarTodas();

        assertNull(response.getBody().get(0).getParMoneda());
    }

    private TransaccionRequestDTO request() {
        TransaccionRequestDTO request = new TransaccionRequestDTO();
        request.setTipoOperacion(TipoOperacion.VENTA);
        request.setClienteId(1L);
        request.setMonedaOrigen("PEN");
        request.setMonedaDestino("USD");
        request.setMontoEntregado(new BigDecimal("1000.00"));
        return request;
    }

    private Cliente cliente() {
        return new Cliente(1L, "Cliente", "70000001", "c@correo.com", "999999999", TipoCliente.NATURAL);
    }

    private ParMoneda par() {
        Moneda pen = new Moneda(1L, "PEN", "Sol", "S/");
        Moneda usd = new Moneda(2L, "USD", "Dolar", "$");
        return new ParMoneda(1L, pen, usd, true);
    }

    private Transaccion transaccion(Long id, EstadoTransaccion estado, boolean conDetalle) {
        Transaccion t = new Transaccion();
        t.setId(id);
        t.setCliente(cliente());
        t.setParMoneda(par());
        t.setTipo(TipoOperacion.VENTA);
        t.setMontoEntregado(new BigDecimal("1000.00"));
        t.setEstado(estado);
        if (conDetalle) {
            t.setDetalle(new DetalleTransaccion(1L, new BigDecimal("3.80"),
                    new BigDecimal("10.00"), new BigDecimal("3800.00")));
        }
        return t;
    }
}
