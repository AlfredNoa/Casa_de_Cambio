package pe.edu.utp.casacambio.modelo.transaccion;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Comprobante generado al completar una transaccion.
 *
 * Sirve como respaldo formal para el cliente y como
 * registro contable para la casa de cambio.
 *
 * Relacion UML: asociacion 1 a 1 con Transaccion.
 */
@Entity
@Table(name = "comprobantes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Slf4j
public class Comprobante {

    /** Identificador unico del comprobante. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Numero de serie unico del comprobante (ej: CC-2025-0001). */
    @Column(nullable = false, unique = true)
    private String numero;

    /** Fecha y hora exacta de emision del comprobante. */
    // Nota SonarQube S1450: este campo es una columna JPA persistida en base
    // de datos y se expone mediante el getter generado por Lombok, por lo que
    // no puede convertirse en variable local sin romper el mapeo objeto-relacional.
    @SuppressWarnings("java:S1450")
    private LocalDateTime fechaEmision;

    /** Transaccion a la que corresponde este comprobante. */
    @OneToOne
    @JoinColumn(name = "transaccion_id", nullable = false)
    private Transaccion transaccion;

    /** Subtotal reflejado en el comprobante. */
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal subtotal;

    /**
     * Genera el comprobante asignando la fecha y hora de emision actual.
     */
    public void generar() {
        this.fechaEmision = LocalDateTime.now(ZoneId.of("America/Lima"));
        log.info("Comprobante generado: {}", this.numero);
    }
}
