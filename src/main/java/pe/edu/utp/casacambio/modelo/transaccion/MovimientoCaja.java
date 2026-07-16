package pe.edu.utp.casacambio.modelo.transaccion;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Registro historico de cada entrada o salida de dinero en una caja.
 *
 * Relacion UML: agregacion con Caja. Los movimientos pueden consultarse
 * independientemente del estado de la caja (auditoria historica).
 *
 * Principio SOLID - SRP: solo registra un movimiento de caja.
 */
@Entity
@Table(name = "movimientos_caja")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Slf4j
public class MovimientoCaja {

    /** Identificador unico del movimiento. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tipo de movimiento: ENTRADA o SALIDA. */
    @Column(nullable = false)
    private String tipo;

    /** Monto involucrado en el movimiento. */
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal monto;

    /** Fecha y hora en que se realizo el movimiento. */
    // Nota SonarQube S1450: este campo es una columna JPA persistida en base
    // de datos y se expone mediante el getter generado por Lombok, por lo que
    // no puede convertirse en variable local sin romper el mapeo objeto-relacional.
    @SuppressWarnings("java:S1450")
    private LocalDateTime fecha;

    /** Descripcion o concepto del movimiento. */
    private String descripcion;

    /** Caja a la que pertenece este movimiento. */
    @ManyToOne
    @JoinColumn(name = "caja_id")
    private Caja caja;

    /**
     * Registra el movimiento asignando la fecha y hora actual.
     */
    public void registrar() {
        this.fecha = LocalDateTime.now(ZoneId.of("America/Lima"));
        log.info("Movimiento registrado: {} | {}", tipo, monto);
    }
}
