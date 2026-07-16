package pe.edu.utp.casacambio.modelo.tipocambio;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Documento generado con informacion consolidada de las operaciones del sistema.
 *
 * Puede ser de tipo DIARIO, SEMANAL o MENSUAL. Usa un FiltroReporte
 * para delimitar el periodo y las condiciones del reporte.
 *
 * Relacion UML: composicion con FiltroReporte (el filtro no existe sin el reporte).
 * Patron a aplicar en entrega final: Builder para la construccion paso a paso.
 */
@Entity
@Table(name = "reportes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Slf4j
public class Reporte {

    /** Identificador unico del reporte. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tipo de reporte: DIARIO, SEMANAL o MENSUAL. */
    @Column(nullable = false)
    private String tipo;

    /** Fecha y hora en que se genero el reporte. */
    // Nota SonarQube S1450: este campo es una columna JPA persistida en base
    // de datos y se expone mediante el getter generado por Lombok, por lo que
    // no puede convertirse en variable local sin romper el mapeo objeto-relacional.
    @SuppressWarnings("java:S1450")
    private LocalDateTime fechaGeneracion;

    /** Usuario que solicito la generacion del reporte. */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario generadoPor;

    /** Filtro que delimita el contenido del reporte (composicion). */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "filtro_id")
    private FiltroReporte filtro;

    /**
     * Genera el reporte aplicando el filtro configurado.
     */
    public void generar() {
        this.fechaGeneracion = LocalDateTime.now(ZoneId.of("America/Lima"));
        log.info("Reporte generado: {}", tipo);
    }

    /**
     * Exporta el reporte al formato de salida configurado.
     */
    public void exportar() {
        log.info("Exportando reporte: {}", tipo);
    }
}
