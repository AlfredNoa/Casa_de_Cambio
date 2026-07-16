package pe.edu.utp.casacambio.modelo.cliente;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa una divisa manejada por la casa de cambio.
 *
 * El sistema opera con tres monedas: Sol peruano (PEN),
 * Dolar americano (USD) y Euro (EUR).
 *
 * Principio SOLID - SRP: solo almacena los datos de una divisa.
 */
@Entity
@Table(name = "monedas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class Moneda {

    /** Identificador unico generado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Codigo ISO de la moneda (PEN, USD, EUR). Maximo 3 caracteres. */
    @Column(nullable = false, unique = true, length = 3)
    private String codigo;

    /** Nombre completo de la moneda. */
    @Column(nullable = false)
    private String nombre;

    /** Simbolo visual de la moneda: S/, $, EUR. */
    @Column(nullable = false, length = 5)
    private String simbolo;

    /**
     * Retorna el codigo ISO de la moneda.
     *
     * @return codigo de la moneda (ej: "PEN", "USD")
     */
    public String getCodigo() {
        return this.codigo;
    }
}
