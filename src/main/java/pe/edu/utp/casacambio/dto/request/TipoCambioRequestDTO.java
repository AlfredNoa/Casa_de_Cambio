package pe.edu.utp.casacambio.dto.request;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO de entrada para registrar/actualizar un tipo de cambio. Se reciben
 * codigos de moneda simples en vez de la entidad ParMoneda completa,
 * por la misma razon que en TransaccionRequestDTO.
 */
@Data
public class TipoCambioRequestDTO {

    /** Codigo ISO de la moneda de origen, ej: "PEN". */
    private String monedaOrigen;

    /** Codigo ISO de la moneda de destino, ej: "USD". */
    private String monedaDestino;

    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
}
