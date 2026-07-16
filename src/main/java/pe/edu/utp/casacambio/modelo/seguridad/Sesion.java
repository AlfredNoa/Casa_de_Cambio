package pe.edu.utp.casacambio.modelo.seguridad;

import pe.edu.utp.casacambio.modelo.tipocambio.Usuario;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Registra el ciclo de vida de una sesion de usuario en el sistema.
 *
 * Almacena la hora de inicio, la hora de cierre y si la sesion
 * sigue activa, permitiendo controlar accesos concurrentes.
 *
 * Principio SOLID - SRP: solo gestiona el estado de una sesion.
 */
@Entity
@Table(name = "sesiones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Slf4j
public class Sesion {

    /** Identificador unico de la sesion. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario al que pertenece esta sesion. */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Fecha y hora en que el usuario inicio sesion. */
    // SonarQube java:S1450: campo JPA persistido en BD; se lee via el getter
    // generado por Lombok una vez existan Controllers/tests que lo consuman.
    @SuppressWarnings("java:S1450")
    private LocalDateTime horaInicio;

    /** Fecha y hora en que el usuario cerro sesion. Null si sigue activa. */
    // SonarQube java:S1450: mismo caso que horaInicio, campo JPA persistido.
    @SuppressWarnings("java:S1450")
    private LocalDateTime horaFin;

    /** Indica si la sesion sigue activa en este momento. */
    @Column(nullable = false)
    private boolean activa;

    /**
     * Inicia la sesion asignando la hora actual y marcandola como activa.
     */
    public void iniciar() {
        this.horaInicio = LocalDateTime.now(ZoneId.of("America/Lima"));
        this.activa = true;
        log.info("Sesion iniciada para: {}", usuario.getUsername());
    }

    /**
     * Cierra la sesion asignando la hora de salida y marcandola como inactiva.
     */
    public void cerrar() {
        this.horaFin = LocalDateTime.now(ZoneId.of("America/Lima"));
        this.activa = false;
        log.info("Sesion cerrada para: {}", usuario.getUsername());
    }
}
