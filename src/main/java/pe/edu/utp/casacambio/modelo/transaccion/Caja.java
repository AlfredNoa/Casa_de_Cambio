package pe.edu.utp.casacambio.modelo.transaccion;

import pe.edu.utp.casacambio.modelo.tipocambio.Sucursal;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el punto de atencion donde los cajeros procesan transacciones.
 *
 * Cada caja pertenece a una sucursal y lleva un registro de todos sus
 * movimientos de entrada y salida de dinero durante el dia.
 *
 * Relacion UML: agregacion con MovimientoCaja (los movimientos
 * sobreviven al cierre de caja para fines de auditoria).
 */
@Entity
@Table(name = "cajas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "movimientos")
@Slf4j
public class Caja {

    /** Identificador unico de la caja. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Saldo con que se abre la caja al inicio del dia. */
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal saldoInicial;

    /** Saldo actual de la caja tras los movimientos del dia. */
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal saldoActual;

    /** Sucursal a la que pertenece esta caja. */
    @ManyToOne
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    /** Indica si la caja esta abierta para procesar operaciones. */
    @Column(nullable = false)
    private boolean abierta;

    /** Lista de movimientos registrados durante el dia (agregacion). */
    @OneToMany(mappedBy = "caja", cascade = CascadeType.ALL)
    private List<MovimientoCaja> movimientos = new ArrayList<>();

    /**
     * Abre la caja para el dia con el saldo inicial configurado.
     */
    public void abrir() {
        this.abierta = true;
        log.info("Caja abierta con saldo inicial: {}", saldoInicial);
    }

    /**
     * Cierra la caja al finalizar el dia.
     */
    public void cerrar() {
        this.abierta = false;
        log.info("Caja cerrada con saldo final: {}", saldoActual);
    }

    /**
     * Agrega un movimiento a la lista de la caja.
     *
     * @param movimiento movimiento a registrar
     */
    public void registrarMovimiento(MovimientoCaja movimiento) {
        movimientos.add(movimiento);
    }
}
