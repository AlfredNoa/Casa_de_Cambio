package pe.edu.utp.casacambio.modelo.notificacion;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Mensaje generado automaticamente ante un evento del sistema.
 *
 * Es el objeto que reciben los observadores en el patron Observer.
 * Cada notificacion tiene un tipo (ALERTA, INFORMATIVA, ERROR)
 * y un canal de envio (PANTALLA, EMAIL, LOG).
 *
 * Patron a implementar en entrega final: Observer (suscriptores reciben Notificacion).
 */
@Entity
@Table(name = "notificaciones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Slf4j
public class Notificacion {

    /** Identificador unico de la notificacion. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Texto del mensaje que se enviara al destinatario. */
    @Column(nullable = false)
    private String mensaje;

    /** Clasificacion de la notificacion: ALERTA, INFORMATIVA o ERROR. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacion tipo;

    /** Canal por el que se enviara: PANTALLA, EMAIL o LOG. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalNotificacion canal;

    /** Fecha y hora en que se genero la notificacion. */
    // Nota SonarQube S1450: este campo es una columna JPA persistida en base
    // de datos y se expone mediante el getter generado por Lombok, por lo que
    // no puede convertirse en variable local sin romper el mapeo objeto-relacional.
    @SuppressWarnings("java:S1450")
    private LocalDateTime fechaHora;

    /** Indica si el destinatario ya leyo la notificacion. */
    private boolean leida;

    /**
     * Envia la notificacion por el canal configurado.
     */
    public void enviar() {
        this.fechaHora = LocalDateTime.now(ZoneId.of("America/Lima"));
        log.info("[{}] {}: {}", canal, tipo, mensaje);
    }
}
