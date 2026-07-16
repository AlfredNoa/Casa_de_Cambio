package pe.edu.utp.casacambio.service.patron.decorator;

import org.junit.jupiter.api.Test;
import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.cliente.TipoCliente;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DescuentoVipDecoratorTest {

    private final CalculadorPrecio base = new CalculadorPrecio() {
        @Override
        public BigDecimal calcularPrecioVenta(TipoCambio tipoCambio, Cliente cliente) {
            return new BigDecimal("4.0000");
        }

        @Override
        public BigDecimal calcularPrecioCompra(TipoCambio tipoCambio, Cliente cliente) {
            return new BigDecimal("3.0000");
        }
    };

    @Test
    void venta_clienteVip_debeAplicarDescuento() {
        Cliente vip = cliente(TipoCliente.VIP);
        BigDecimal resultado = new DescuentoVipDecorator(base).calcularPrecioVenta(new TipoCambio(), vip);
        assertEquals(new BigDecimal("3.9880"), resultado);
    }

    @Test
    void compra_clienteVip_debeAplicarBono() {
        Cliente vip = cliente(TipoCliente.VIP);
        BigDecimal resultado = new DescuentoVipDecorator(base).calcularPrecioCompra(new TipoCambio(), vip);
        assertEquals(new BigDecimal("3.0090"), resultado);
    }

    @Test
    void clienteNatural_noDebeModificarPrecios() {
        Cliente natural = cliente(TipoCliente.NATURAL);
        DescuentoVipDecorator decorator = new DescuentoVipDecorator(base);
        assertEquals(new BigDecimal("4.0000"), decorator.calcularPrecioVenta(new TipoCambio(), natural));
        assertEquals(new BigDecimal("3.0000"), decorator.calcularPrecioCompra(new TipoCambio(), natural));
    }

    @Test
    void clienteNulo_noDebeModificarPrecios() {
        DescuentoVipDecorator decorator = new DescuentoVipDecorator(base);
        assertEquals(new BigDecimal("4.0000"), decorator.calcularPrecioVenta(new TipoCambio(), null));
        assertEquals(new BigDecimal("3.0000"), decorator.calcularPrecioCompra(new TipoCambio(), null));
    }

    private Cliente cliente(TipoCliente tipo) {
        Cliente cliente = new Cliente();
        cliente.setTipoCliente(tipo);
        return cliente;
    }
}
