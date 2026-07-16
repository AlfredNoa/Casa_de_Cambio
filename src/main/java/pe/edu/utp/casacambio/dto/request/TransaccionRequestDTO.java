package pe.edu.utp.casacambio.dto.request;

import lombok.Data;
import pe.edu.utp.casacambio.modelo.cliente.TipoOperacion;
import java.math.BigDecimal;

/**
 * DTO de entrada para crear una transaccion. Se reciben codigos de
 * moneda simples (ej: "PEN", "USD") en vez de la entidad ParMoneda
 * completa, para no exponer el modelo de persistencia en el contrato
 * de la API ni obligar al cliente a enviar un JSON anidado.
 */
@Data
public class TransaccionRequestDTO {
    private TipoOperacion tipoOperacion;
    private Long clienteId;

    /** Codigo ISO de la moneda de origen, ej: "PEN". */
    private String monedaOrigen;

    /** Codigo ISO de la moneda de destino, ej: "USD". */
    private String monedaDestino;

    private BigDecimal montoEntregado;
}
