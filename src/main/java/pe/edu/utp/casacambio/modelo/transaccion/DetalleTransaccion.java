package pe.edu.utp.casacambio.modelo.transaccion;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;

/**
 * Detalle economico de una transaccion: tipo de cambio aplicado,
 * comision cobrada y monto final entregado al cliente.
 *
 * Relacion UML: composicion con Transaccion. Este detalle no tiene
 * sentido sin la transaccion a la que pertenece.
 *
 * Principio SOLID - SRP: solo calcula y almacena el desglose economico.
 */
@Entity
@Table(name = "detalles_transaccion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Slf4j
public class DetalleTransaccion {

    /** Identificador unico del detalle. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tipo de cambio que estaba vigente al momento de la transaccion. */
    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal tipoCambioAplicado;

    /** Comision cobrada por el servicio de cambio de divisa. */
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal comision;

    /** Monto final que recibe el cliente despues de aplicar tipo de cambio y comision. */
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal montoFinal;

    /**
     * Calcula el monto final aplicando el tipo de cambio y restando la comision.
     */
    public void calcular() {
        log.info("Calculando detalle de transaccion...");
    }
}
