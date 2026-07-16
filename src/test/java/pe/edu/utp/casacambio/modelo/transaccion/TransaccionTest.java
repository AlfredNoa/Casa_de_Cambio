package pe.edu.utp.casacambio.modelo.transaccion;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransaccionTest {

    @Test
    void calcularRecibido_conDetalle_debeRetornarMontoFinal() {
        Transaccion transaccion = new Transaccion();
        DetalleTransaccion detalle = new DetalleTransaccion();
        detalle.setMontoFinal(new BigDecimal("123.45"));
        transaccion.setDetalle(detalle);

        assertEquals(new BigDecimal("123.45"), transaccion.calcularRecibido());
    }

    @Test
    void calcularRecibido_sinDetalle_debeRetornarCero() {
        assertEquals(BigDecimal.ZERO, new Transaccion().calcularRecibido());
    }

    @Test
    void confirmar_debeCambiarEstado() {
        Transaccion transaccion = new Transaccion();
        transaccion.setEstado(EstadoTransaccion.PENDIENTE);
        transaccion.confirmar();
        assertEquals(EstadoTransaccion.COMPLETADA, transaccion.getEstado());
    }

    @Test
    void anular_debeCambiarEstado() {
        Transaccion transaccion = new Transaccion();
        transaccion.setEstado(EstadoTransaccion.PENDIENTE);
        transaccion.anular();
        assertEquals(EstadoTransaccion.ANULADA, transaccion.getEstado());
    }
}
