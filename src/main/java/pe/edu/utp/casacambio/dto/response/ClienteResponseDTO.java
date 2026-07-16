package pe.edu.utp.casacambio.dto.response;

import lombok.Data;
import pe.edu.utp.casacambio.modelo.cliente.TipoCliente;

@Data
public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String dni;
    private String email;
    private String telefono;
    private TipoCliente tipoCliente;
}
