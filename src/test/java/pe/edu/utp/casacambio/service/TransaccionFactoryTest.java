package pe.edu.utp.casacambio.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.casacambio.modelo.cliente.TipoOperacion;
import pe.edu.utp.casacambio.modelo.transaccion.EstadoTransaccion;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;
import pe.edu.utp.casacambio.service.patron.factory.TransaccionFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el patrón Factory Method {@link TransaccionFactory}.
 */
@DisplayName("Tests de TransaccionFactory (Factory Method)")
class TransaccionFactoryTest {

    @Test
    @DisplayName("Debe crear una transacción de COMPRA con estado PENDIENTE")
    void debeCrearTransaccionCompra() {
        Transaccion t = TransaccionFactory.crear(TipoOperacion.COMPRA);
        assertNotNull(t);
        assertEquals(TipoOperacion.COMPRA, t.getTipo());
        assertEquals(EstadoTransaccion.PENDIENTE, t.getEstado());
    }

    @Test
    @DisplayName("Debe crear una transacción de VENTA con estado PENDIENTE")
    void debeCrearTransaccionVenta() {
        Transaccion t = TransaccionFactory.crear(TipoOperacion.VENTA);
        assertNotNull(t);
        assertEquals(TipoOperacion.VENTA, t.getTipo());
        assertEquals(EstadoTransaccion.PENDIENTE, t.getEstado());
    }

    @Test
    @DisplayName("Debe crear una transacción de TRANSFERENCIA con estado EN_PROCESO")
    void debeCrearTransaccionTransferencia() {
        Transaccion t = TransaccionFactory.crear(TipoOperacion.TRANSFERENCIA);
        assertNotNull(t);
        assertEquals(TipoOperacion.TRANSFERENCIA, t.getTipo());
        assertEquals(EstadoTransaccion.EN_PROCESO, t.getEstado());
    }

    @Test
    @DisplayName("Cada llamada al factory debe crear una instancia distinta")
    void cadaLlamadaDebeCrearInstanciaDistinta() {
        Transaccion t1 = TransaccionFactory.crear(TipoOperacion.COMPRA);
        Transaccion t2 = TransaccionFactory.crear(TipoOperacion.COMPRA);
        assertNotSame(t1, t2);
    }
}
