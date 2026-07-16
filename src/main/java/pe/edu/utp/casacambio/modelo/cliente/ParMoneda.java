package pe.edu.utp.casacambio.modelo.cliente;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa un par de monedas para el que existe un tipo de cambio.
 *
 * Ejemplos de pares validos: PEN/USD, PEN/EUR, USD/EUR.
 * Cada par combina una moneda de origen y una de destino.
 *
 * Relacion UML: composicion con Moneda (requiere exactamente 2 instancias).
 * Principio SOLID - SRP: solo modela la relacion entre dos divisas.
 */
@Entity
@Table(name = "pares_moneda")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class ParMoneda {

    /** Identificador unico del par. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Moneda desde la que se convierte. */
    @ManyToOne
    @JoinColumn(name = "moneda_origen_id", nullable = false)
    private Moneda monedaOrigen;

    /** Moneda hacia la que se convierte. */
    @ManyToOne
    @JoinColumn(name = "moneda_destino_id", nullable = false)
    private Moneda monedaDestino;

    /** Indica si este par esta habilitado para operar actualmente. */
    @Column(nullable = false)
    private boolean activo;

    /**
     * Retorna la representacion del par en formato "ORIGEN/DESTINO".
     *
     * @return par de monedas como String (ej: "PEN/USD")
     */
    public String getPar() {
        return monedaOrigen.getCodigo() + "/" + monedaDestino.getCodigo();
    }
}
