package pe.edu.utp.casacambio.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TipoCambioResponseDTO {
    private String parMoneda;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private LocalDate fechaRegistro;
}
