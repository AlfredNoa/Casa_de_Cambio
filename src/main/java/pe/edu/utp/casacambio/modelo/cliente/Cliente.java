package pe.edu.utp.casacambio.modelo.cliente;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Representa a un cliente que realiza operaciones en la casa de cambio.
 * Puede ser una persona natural, juridica o VIP.
 *
 * Principio SOLID - SRP: esta clase solo gestiona los datos del cliente.
 * La logica de negocio (registrar, validar) reside en ClienteService.
 */
@Entity
@Table(name = "clientes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Slf4j
public class Cliente {

    /** Identificador unico generado automaticamente por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre completo del cliente. No puede ser nulo. */
    @Column(nullable = false)
    private String nombre;

    /** Numero de documento de identidad. Unico en el sistema. */
    @Column(nullable = false, unique = true)
    private String dni;

    /** Correo electronico de contacto. Opcional. */
    private String email;

    /** Numero de telefono de contacto. Opcional. */
    private String telefono;

    /** Clasificacion del cliente: NATURAL, JURIDICO o VIP. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCliente tipoCliente;

    /**
     * Registra al cliente en el sistema emitiendo un log informativo.
     */
    public void registrar() {
        log.info("Cliente registrado: {}", this.nombre);
    }
}
