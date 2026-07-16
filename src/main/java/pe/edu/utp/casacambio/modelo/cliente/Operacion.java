package pe.edu.utp.casacambio.modelo.cliente;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Clase abstracta base para todas las operaciones de la casa de cambio.
 *
 * Toda operacion concreta (compra, venta, transferencia) debe extender
 * esta clase e implementar sus metodos abstractos con la logica propia.
 *
 * Principio SOLID - OCP: abierta para extension (nuevas operaciones),
 * cerrada para modificacion del codigo existente.
 * Principio SOLID - LSP: cualquier subclase sustituye a Operacion
 * sin alterar el comportamiento del sistema.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "operaciones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public abstract class Operacion {

    /** Identificador unico de la operacion. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha en que se realizo la operacion. */
    @Column(nullable = false)
    private LocalDate fecha;

    /** Monto base de la operacion antes de aplicar tipo de cambio. */
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal monto;

    /** Tipo de operacion: COMPRA, VENTA o TRANSFERENCIA. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOperacion tipoOperacion;

    /**
     * Calcula el total de la operacion segun las reglas del tipo concreto.
     *
     * @return monto total calculado
     */
    public abstract BigDecimal calcularTotal();

    /**
     * Ejecuta la operacion en el sistema.
     */
    public abstract void ejecutar();
}
