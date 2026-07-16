package pe.edu.utp.casacambio.modelo.seguridad;

import pe.edu.utp.casacambio.modelo.tipocambio.Usuario;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Registro de auditoria de accesos al sistema por usuario.
 *
 * Almacena cada intento de LOGIN o LOGOUT, incluyendo si fue
 * exitoso y desde que direccion IP se realizo el acceso.
 *
 * Principio SOLID - SRP: solo registra eventos de acceso al sistema.
 */
@Entity
@Table(name = "logs_acceso")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Slf4j
public class LogAcceso {

    /** Identificador unico del registro de acceso. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario que intento acceder al sistema. */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Fecha y hora exacta del intento de acceso. */
    // Nota SonarQube S1450: este campo es una columna JPA persistida en base
    // de datos y se expone mediante el getter generado por Lombok, por lo que
    // no puede convertirse en variable local sin romper el mapeo objeto-relacional.
    @SuppressWarnings("java:S1450")
    private LocalDateTime fechaHora;

    /** Tipo de accion registrada: LOGIN o LOGOUT. */
    @Column(nullable = false)
    private String accion;

    /** Direccion IP desde donde se realizo el acceso. */
    private String ipOrigen;

    /** Indica si el intento de acceso fue exitoso. */
    private boolean exitoso;

    /**
     * Registra el evento de acceso con la fecha y hora actual.
     */
    public void registrar() {
        this.fechaHora = LocalDateTime.now(ZoneId.of("America/Lima"));
        log.info("Log de acceso: {} - {}", accion, usuario.getUsername());
    }
}
