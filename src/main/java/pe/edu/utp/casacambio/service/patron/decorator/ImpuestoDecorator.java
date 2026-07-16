package pe.edu.utp.casacambio.service.patron.decorator;

import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * ConcreteDecorator: agrega un cargo administrativo (impuesto) solo al
 * precio de venta, ya que es el que finalmente paga el cliente.
 * El precio de compra (lo que la casa paga al cliente) no lleva este cargo.
 *
 * Patron: Decorator.
 */
@Slf4j
public class ImpuestoDecorator extends CalculadorPrecioDecorator {

    /** Porcentaje de cargo administrativo: 1%. */
    private static final BigDecimal PORCENTAJE_IMPUESTO = new BigDecimal("0.01");

    public ImpuestoDecorator(CalculadorPrecio componente) {
        super(componente);
    }

    @Override
    public BigDecimal calcularPrecioVenta(TipoCambio tipoCambio, Cliente cliente) {
        BigDecimal precioPrevio = super.calcularPrecioVenta(tipoCambio, cliente);
        BigDecimal precioConImpuesto = precioPrevio
                .add(precioPrevio.multiply(PORCENTAJE_IMPUESTO))
                .setScale(4, RoundingMode.HALF_UP);
        log.info("Decorator Impuesto: precio venta {} -> {}", precioPrevio, precioConImpuesto);
        return precioConImpuesto;
    }

    // calcularPrecioCompra no se sobrescribe: el impuesto no aplica al
    // comprar la moneda extranjera del cliente, solo al vendersela.
}
