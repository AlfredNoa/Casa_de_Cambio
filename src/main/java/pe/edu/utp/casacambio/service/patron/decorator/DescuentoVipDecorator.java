package pe.edu.utp.casacambio.service.patron.decorator;

import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.cliente.TipoCliente;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * ConcreteDecorator: aplica un descuento adicional solo si el cliente
 * es de tipo VIP (mejor precio de venta, mejor precio de compra).
 * Para clientes NATURAL o JURIDICO delega sin cambios.
 *
 * Patron: Decorator.
 */
@Slf4j
public class DescuentoVipDecorator extends CalculadorPrecioDecorator {

    /** Porcentaje de beneficio VIP: 0.3%. */
    private static final BigDecimal PORCENTAJE_DESCUENTO_VIP = new BigDecimal("0.003");

    public DescuentoVipDecorator(CalculadorPrecio componente) {
        super(componente);
    }

    @Override
    public BigDecimal calcularPrecioVenta(TipoCambio tipoCambio, Cliente cliente) {
        BigDecimal precioPrevio = super.calcularPrecioVenta(tipoCambio, cliente);
        if (esVip(cliente)) {
            BigDecimal precioConDescuento = precioPrevio
                    .subtract(precioPrevio.multiply(PORCENTAJE_DESCUENTO_VIP))
                    .setScale(4, RoundingMode.HALF_UP);
            log.info("Decorator DescuentoVip: precio venta {} -> {}", precioPrevio, precioConDescuento);
            return precioConDescuento;
        }
        return precioPrevio;
    }

    @Override
    public BigDecimal calcularPrecioCompra(TipoCambio tipoCambio, Cliente cliente) {
        BigDecimal precioPrevio = super.calcularPrecioCompra(tipoCambio, cliente);
        if (esVip(cliente)) {
            BigDecimal precioConBono = precioPrevio
                    .add(precioPrevio.multiply(PORCENTAJE_DESCUENTO_VIP))
                    .setScale(4, RoundingMode.HALF_UP);
            log.info("Decorator DescuentoVip: precio compra {} -> {}", precioPrevio, precioConBono);
            return precioConBono;
        }
        return precioPrevio;
    }

    private boolean esVip(Cliente cliente) {
        return cliente != null && cliente.getTipoCliente() == TipoCliente.VIP;
    }
}
