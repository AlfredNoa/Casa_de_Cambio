package pe.edu.utp.casacambio.modelo.cliente;

/**
 * Tipos de cliente que puede atender la casa de cambio.
 *
 * Cada tipo puede implicar condiciones distintas en las operaciones,
 * como descuentos especiales para clientes VIP.
 *
 * Patron aplicado: usado por Factory Method para determinar el tipo de operacion.
 */
public enum TipoCliente {
    NATURAL,
    JURIDICO,
    VIP
}
