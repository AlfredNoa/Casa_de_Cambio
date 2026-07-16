package pe.edu.utp.casacambio.modelo.tipocambio;

import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Precio de compra y venta vigente para un par de monedas en un momento dado.
 *
 * Es el dato central del negocio: define cuanto paga la casa de cambio
 * por la moneda extranjera (compra) y cuanto cobra al venderla (venta).
 *
 * Patron Singleton: GestorTipoCambio administra la instancia vigente del dia.
 * Principio SOLID - SRP: solo almacena y expone los precios del tipo de cambio.
 */
@Entity
@Table(name = "tipos_cambio")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class TipoCambio {

    /** Identificador unico del tipo de cambio. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Par de monedas al que aplica este tipo de cambio (ej: PEN/USD). */
    @ManyToOne
    @JoinColumn(name = "par_moneda_id", nullable = false)
    private ParMoneda parMoneda;

    /** Precio al que la casa de cambio compra la moneda extranjera. */
    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal precioCompra;

    /** Precio al que la casa de cambio vende la moneda extranjera. */
    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal precioVenta;

    /** Fecha y hora exacta en que se registro este tipo de cambio. */
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    /**
     * Retorna el precio de compra vigente para este par.
     *
     * @return precio de compra
     */
    public BigDecimal getPrecioCompra() {
        return this.precioCompra;
    }

    /**
     * Retorna el precio de venta vigente para este par.
     *
     * @return precio de venta
     */
    public BigDecimal getPrecioVenta() {
        return this.precioVenta;
    }

    /**
     * Verifica si este tipo de cambio corresponde al dia de hoy,
     * usando la zona horaria de Peru y el reloj del sistema.
     *
     * @return true si fue registrado en el dia actual
     */
    public boolean esVigente() {
        return esVigente(Clock.system(ZoneId.of("America/Lima")));
    }

    /**
     * Verifica si este tipo de cambio corresponde al dia de hoy segun el
     * reloj indicado. Se expone con Clock inyectable (en vez de usar
     * LocalDateTime.now() directamente) para permitir pruebas unitarias
     * deterministicas sin depender del reloj real del sistema (SonarQube S8692).
     *
     * @param clock reloj a partir del cual se calcula el dia actual
     * @return true si fue registrado en el dia actual segun el reloj dado
     */
    public boolean esVigente(Clock clock) {
        return fechaHora.toLocalDate().isEqual(LocalDateTime.now(clock).toLocalDate());
    }
}
