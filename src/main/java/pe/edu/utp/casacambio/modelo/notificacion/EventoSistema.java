package pe.edu.utp.casacambio.modelo.notificacion;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Evento ocurrido en el sistema que puede disparar notificaciones.
 *
 * Es el "sujeto" del patron Observer: cuando se publica,
 * todos los observadores registrados reciben una Notificacion.
 *
 * Ejemplos de eventos: TIPO_CAMBIO_ACTUALIZADO, TRANSACCION_COMPLETADA, CAJA_CERRADA.
 * Patron a implementar en entrega final: Observer.
 */
@Entity
@Table(name = "eventos_sistema")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Slf4j
public class EventoSistema {

    /** Identificador unico del evento. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre descriptivo del evento (ej: TIPO_CAMBIO_ACTUALIZADO). */
    @Column(nullable = false)
    private String nombre;

    /** Descripcion del contexto en que ocurrio el evento. */
    private String descripcion;

    /** Fecha y hora exacta en que ocurrio el evento. */
    // Nota SonarQube S1450: este campo es una columna JPA persistida en base
    // de datos y se expone mediante el getter generado por Lombok, por lo que
    // no puede convertirse en variable local sin romper el mapeo objeto-relacional.
    @SuppressWarnings("java:S1450")
    private LocalDateTime fechaHora;

    /** Nombre de la clase que genero el evento (para trazabilidad). */
    private String origenClase;

    /**
     * Publica el evento asignando la fecha y hora actual.
     */
    public void publicar() {
        this.fechaHora = LocalDateTime.now(ZoneId.of("America/Lima"));
        log.info("Evento publicado: {} desde {}", nombre, origenClase);
    }
}
