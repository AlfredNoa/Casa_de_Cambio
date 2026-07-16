package pe.edu.utp.casacambio.service.patron.decorator;

import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;

import java.math.BigDecimal;

/**
 * Decorator abstracto del patron: envuelve a otro CalculadorPrecio
 * (el "componente") y por defecto delega en el antes de que la subclase
 * concreta aplique su propio ajuste. Cada decorador concreto solo
 * sobrescribe el metodo que realmente necesita modificar.
 *
 * Patron: Decorator.
 * Principio SOLID - SRP: cada decorador concreto se encarga de un unico
 * tipo de cargo (comision, descuento VIP o impuesto).
 */
public abstract class CalculadorPrecioDecorator implements CalculadorPrecio {

    /** Componente envuelto: puede ser el calculador base u otro decorador. */
    protected final CalculadorPrecio componente;

    protected CalculadorPrecioDecorator(CalculadorPrecio componente) {
        this.componente = componente;
    }

    @Override
    public BigDecimal calcularPrecioVenta(TipoCambio tipoCambio, Cliente cliente) {
        return componente.calcularPrecioVenta(tipoCambio, cliente);
    }

    @Override
    public BigDecimal calcularPrecioCompra(TipoCambio tipoCambio, Cliente cliente) {
        return componente.calcularPrecioCompra(tipoCambio, cliente);
    }
}
