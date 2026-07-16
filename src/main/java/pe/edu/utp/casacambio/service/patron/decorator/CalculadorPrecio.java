package pe.edu.utp.casacambio.service.patron.decorator;

import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;

import java.math.BigDecimal;

/**
 * Component del patron Decorator: calcula el precio final de compra/venta
 * de divisas a partir de un TipoCambio base, permitiendo agregar comisiones,
 * descuentos e impuestos de forma dinamica y combinable, sin tocar la
 * clase TipoCambio ni el calculo base.
 *
 * Patron: Decorator (estructural).
 * Principio SOLID - OCP: se agregan cargos nuevos creando otro decorador,
 * sin modificar el calculo base ni los decoradores existentes.
 */
public interface CalculadorPrecio {

    /**
     * Calcula el precio de venta final: lo que paga el cliente por
     * comprar moneda extranjera.
     *
     * @param tipoCambio tipo de cambio vigente
     * @param cliente cliente que realiza la operacion (puede afectar el precio)
     * @return precio de venta final
     */
    BigDecimal calcularPrecioVenta(TipoCambio tipoCambio, Cliente cliente);

    /**
     * Calcula el precio de compra final: lo que la casa de cambio paga
     * al cliente por su moneda extranjera.
     *
     * @param tipoCambio tipo de cambio vigente
     * @param cliente cliente que realiza la operacion (puede afectar el precio)
     * @return precio de compra final
     */
    BigDecimal calcularPrecioCompra(TipoCambio tipoCambio, Cliente cliente);
}
