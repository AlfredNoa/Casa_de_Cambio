package pe.edu.utp.casacambio.modelo.notificacion;

import pe.edu.utp.casacambio.modelo.tipocambio.Usuario;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Traza detallada de cada accion realizada por un usuario en el sistema.
 *
 * Registra quien hizo que, sobre cual entidad y en que momento,
 * proporcionando trazabilidad completa de las operaciones criticas.
 *
 * Principio SOLID - SRP: unica responsabilidad es registrar acciones auditables.
 */
@Entity
@Table(name = "registros_auditoria")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Slf4j
public class RegistroAuditoria {

    /** Identificador unico del registro de auditoria. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario que realizo la accion registrada. */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    /** Descripcion de la accion realizada (ej: CREAR_TRANSACCION). */
    @Column(nullable = false)
    private String accion;

    /** Nombre de la entidad sobre la que se aplico la accion. */
    @Column(nullable = false)
    private String entidadAfectada;

    /** ID especifico del objeto que fue afectado por la accion. */
    private Long idEntidadAfectada;

    /** Fecha y hora exacta en que se realizo la accion. */
    // Nota SonarQube S1450: este campo es una columna JPA persistida en base
    // de datos y se expone mediante el getter generado por Lombok, por lo que
    // no puede convertirse en variable local sin romper el mapeo objeto-relacional.
    @SuppressWarnings("java:S1450")
    private LocalDateTime fechaHora;

    /** Informacion adicional relevante sobre la accion. */
    private String detalles;

    /**
     * Persiste el registro de auditoria con la fecha y hora actual.
     * Si no hay un usuario especifico asociado (ej: evento generado por
     * el propio sistema, no por una accion de un usuario), se registra
     * como "SISTEMA" en el log en vez de fallar.
     */
    public void guardar() {
        this.fechaHora = LocalDateTime.now(ZoneId.of("America/Lima"));
        String actor = (usuario != null) ? usuario.getUsername() : "SISTEMA";
        log.info("Auditoria: {} -> {} en {}", actor, accion, entidadAfectada);
    }
}
