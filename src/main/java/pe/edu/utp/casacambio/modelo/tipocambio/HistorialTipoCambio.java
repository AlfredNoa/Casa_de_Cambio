package pe.edu.utp.casacambio.modelo.tipocambio;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Registro historico de cada actualizacion del tipo de cambio realizada en el dia.
 *
 * Permite auditar quien actualizo el tipo de cambio, cuando lo hizo
 * y cuales eran los valores anteriores y nuevos.
 *
 * Relacion UML: muchos registros de historial pueden apuntar a un mismo TipoCambio.
 */
@Entity
@Table(name = "historial_tipos_cambio")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Slf4j
public class HistorialTipoCambio {

    /** Identificador unico del registro historico. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tipo de cambio que fue registrado o modificado. */
    @ManyToOne
    @JoinColumn(name = "tipo_cambio_id", nullable = false)
    private TipoCambio tipoCambio;

    /** Fecha y hora exacta del registro en el historial. */
    // Nota SonarQube S1450: este campo es una columna JPA persistida en base
    // de datos y se expone mediante el getter generado por Lombok, por lo que
    // no puede convertirse en variable local sin romper el mapeo objeto-relacional.
    @SuppressWarnings("java:S1450")
    private LocalDateTime fechaRegistro;

    /** Usuario que realizo la actualizacion del tipo de cambio. */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    /**
     * Guarda el registro de historial con la fecha y hora actual.
     */
    public void registrar() {
        this.fechaRegistro = LocalDateTime.now(ZoneId.of("America/Lima"));
        log.info("Historial registrado para tipo de cambio id: {}", tipoCambio.getId());
    }
}
