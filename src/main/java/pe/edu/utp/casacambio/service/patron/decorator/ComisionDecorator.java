package pe.edu.utp.casacambio.service.patron.decorator;

import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * ConcreteDecorator: agrega la comision del servicio de cambio, que es
 * el margen de ganancia de la casa de cambio. Encarece el precio de
 * venta y abarata el precio de compra en el mismo porcentaje.
 *
 * Patron: Decorator.
 */
@Slf4j
public class ComisionDecorator extends CalculadorPrecioDecorator {

    /** Porcentaje de comision del servicio: 0.5%. */
    private static final BigDecimal PORCENTAJE_COMISION = new BigDecimal("0.005");

    public ComisionDecorator(CalculadorPrecio componente) {
        super(componente);
    }

    @Override
    public BigDecimal calcularPrecioVenta(TipoCambio tipoCambio, Cliente cliente) {
        BigDecimal precioPrevio = super.calcularPrecioVenta(tipoCambio, cliente);
        BigDecimal precioConComision = precioPrevio
                .add(precioPrevio.multiply(PORCENTAJE_COMISION))
                .setScale(4, RoundingMode.HALF_UP);
        log.info("Decorator Comision: precio venta {} -> {}", precioPrevio, precioConComision);
        return precioConComision;
    }

    @Override
    public BigDecimal calcularPrecioCompra(TipoCambio tipoCambio, Cliente cliente) {
        BigDecimal precioPrevio = super.calcularPrecioCompra(tipoCambio, cliente);
        BigDecimal precioConComision = precioPrevio
                .subtract(precioPrevio.multiply(PORCENTAJE_COMISION))
                .setScale(4, RoundingMode.HALF_UP);
        log.info("Decorator Comision: precio compra {} -> {}", precioPrevio, precioConComision);
        return precioConComision;
    }
}
