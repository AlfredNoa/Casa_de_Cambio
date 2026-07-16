package pe.edu.utp.casacambio.modelo.cliente;

/**
 * Tipos de operacion disponibles en la casa de cambio.
 *
 * Usado por TransaccionFactory para seleccionar el creator correspondiente
 * y crear la transaccion con la configuracion adecuada.
 */
public enum TipoOperacion {
    COMPRA,
    VENTA,
    TRANSFERENCIA
}
