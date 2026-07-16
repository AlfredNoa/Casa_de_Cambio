package pe.edu.utp.casacambio.modelo.transaccion;

import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import pe.edu.utp.casacambio.modelo.cliente.TipoOperacion;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Representa una operacion concreta de compra o venta de divisas
 * realizada por un cliente en ventanilla.
 *
 * Principio SOLID - SRP: solo almacena el estado de la transaccion.
 * La creacion es responsabilidad de TransaccionFactory.
 * El calculo economico lo realiza DetalleTransaccion.
 */
@Entity
@Table(name = "transacciones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class Transaccion {

    /** Identificador unico de la transaccion. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cliente que realiza la operacion de cambio. */
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /** Par de monedas involucrado en la operacion (ej: PEN/USD). */
    @ManyToOne
    @JoinColumn(name = "par_moneda_id", nullable = false)
    private ParMoneda parMoneda;

    /** Tipo de operacion: COMPRA, VENTA o TRANSFERENCIA. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOperacion tipo;

    /** Monto en moneda de origen que entrega el cliente. */
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal montoEntregado;

    /** Estado actual de la transaccion en su ciclo de vida. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTransaccion estado;

    /** Detalle economico de la transaccion (composicion). */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "detalle_id")
    private DetalleTransaccion detalle;

    /**
     * Calcula el monto que recibira el cliente segun el tipo de cambio aplicado.
     *
     * @return monto a recibir, o BigDecimal.ZERO si no hay detalle cargado
     */
    public BigDecimal calcularRecibido() {
        if (detalle != null) {
            return detalle.getMontoFinal();
        }
        return BigDecimal.ZERO;
    }

    /**
     * Confirma la transaccion cambiando su estado a COMPLETADA.
     */
    public void confirmar() {
        this.estado = EstadoTransaccion.COMPLETADA;
    }

    /**
     * Anula la transaccion cambiando su estado a ANULADA.
     */
    public void anular() {
        this.estado = EstadoTransaccion.ANULADA;
    }
}
