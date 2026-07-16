package pe.edu.utp.casacambio.dto.response;

import lombok.Data;
import pe.edu.utp.casacambio.modelo.transaccion.EstadoTransaccion;
import java.math.BigDecimal;

@Data
public class TransaccionResponseDTO {
    private Long id;
    private String tipoOperacion;
    private String parMoneda;
    private BigDecimal montoRecibido; // calculado final
    private BigDecimal montoEntregado;
    private BigDecimal tipoCambioUsado;
    private BigDecimal comision;
    private EstadoTransaccion estado;
}

