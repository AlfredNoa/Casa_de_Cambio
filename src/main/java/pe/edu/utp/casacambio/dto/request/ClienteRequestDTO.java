package pe.edu.utp.casacambio.dto.request;

import lombok.Data;
import pe.edu.utp.casacambio.modelo.cliente.TipoCliente;

@Data
public class ClienteRequestDTO {
    private String nombre;
    private String dni;
    private String email;
    private String telefono;

    /** Tipo de cliente (NATURAL, JURIDICO, VIP). Si no se envia, se asume NATURAL. */
    private TipoCliente tipoCliente;
}
