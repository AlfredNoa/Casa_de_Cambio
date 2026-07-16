package pe.edu.utp.casacambio.service.patron.decorator;

import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;

import java.math.BigDecimal;

/**
 * ConcreteComponent del patron Decorator: retorna el precio base del
 * TipoCambio tal cual, sin ningun cargo o descuento adicional.
 * Es el punto de partida sobre el que se van envolviendo los decoradores.
 *
 * Patron: Decorator.
 */
public class CalculadorPrecioBase implements CalculadorPrecio {

    @Override
    public BigDecimal calcularPrecioVenta(TipoCambio tipoCambio, Cliente cliente) {
        return tipoCambio.getPrecioVenta();
    }

    @Override
    public BigDecimal calcularPrecioCompra(TipoCambio tipoCambio, Cliente cliente) {
        return tipoCambio.getPrecioCompra();
    }
}
