package pe.edu.utp.casacambio.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.utp.casacambio.dto.request.TipoCambioRequestDTO;
import pe.edu.utp.casacambio.dto.response.TipoCambioResponseDTO;
import pe.edu.utp.casacambio.service.patron.singleton.GestorTipoCambio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class TipoCambioControllerTest {

    private TipoCambioController controller;

    @BeforeEach
    void setUp() {
        controller = new TipoCambioController();
        GestorTipoCambio.getInstancia().limpiarTiposCambio();
    }

    @AfterEach
    void limpiar() {
        GestorTipoCambio.getInstancia().limpiarTiposCambio();
    }

    @Test
    void actualizarYObtener_debeRetornarTipoVigente() {
        TipoCambioRequestDTO request = new TipoCambioRequestDTO();
        request.setMonedaOrigen("PEN");
        request.setMonedaDestino("USD");
        request.setPrecioCompra(new BigDecimal("3.72"));
        request.setPrecioVenta(new BigDecimal("3.78"));

        ResponseEntity<TipoCambioResponseDTO> creado = controller.actualizarTipoCambio(request);
        ResponseEntity<TipoCambioResponseDTO> obtenido = controller.obtenerTipoCambio("PEN", "USD");

        assertEquals(HttpStatus.OK, creado.getStatusCode());
        assertNotNull(creado.getBody());
        assertEquals("PEN/USD", creado.getBody().getParMoneda());
        assertEquals(LocalDate.now(ZoneId.of("America/Lima")), creado.getBody().getFechaRegistro());
        assertEquals(HttpStatus.OK, obtenido.getStatusCode());
        assertNotNull(obtenido.getBody());
        assertEquals(new BigDecimal("3.78"), obtenido.getBody().getPrecioVenta());
    }

    @Test
    void obtener_sinRegistro_debeRetornarNotFound() {
        ResponseEntity<TipoCambioResponseDTO> response = controller.obtenerTipoCambio("PEN", "EUR");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void limpiar_debeEliminarTiposRegistrados() {
        TipoCambioRequestDTO request = new TipoCambioRequestDTO();
        request.setMonedaOrigen("PEN");
        request.setMonedaDestino("USD");
        request.setPrecioCompra(new BigDecimal("3.70"));
        request.setPrecioVenta(new BigDecimal("3.80"));
        controller.actualizarTipoCambio(request);

        ResponseEntity<Void> limpieza = controller.limpiarTiposCambio();
        ResponseEntity<TipoCambioResponseDTO> consulta = controller.obtenerTipoCambio("PEN", "USD");

        assertEquals(HttpStatus.OK, limpieza.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, consulta.getStatusCode());
    }
}
