package pe.edu.utp.casacambio.modelo.tipocambio;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una sede fisica de la casa de cambio.
 *
 * Cada sucursal puede tener multiples cajas operativas
 * y varios usuarios (cajeros y supervisores) asignados.
 *
 * Principio SOLID - SRP: solo almacena los datos de una sucursal.
 */
@Entity
@Table(name = "sucursales")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class Sucursal {

    /** Identificador unico de la sucursal. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de la sucursal (ej: "Sede Central", "Sucursal Miraflores"). */
    @Column(nullable = false)
    private String nombre;

    /** Direccion fisica de la sucursal. */
    @Column(nullable = false)
    private String direccion;

    /** Indica si la sucursal esta operativa actualmente. */
    @Column(nullable = false)
    private boolean activa;

    /**
     * Retorna la lista de cajas asociadas a esta sucursal.
     *
     * @return lista de cajas de la sucursal
     */
    public List<Object> listarCajas() {
        return new ArrayList<>();
    }
}
