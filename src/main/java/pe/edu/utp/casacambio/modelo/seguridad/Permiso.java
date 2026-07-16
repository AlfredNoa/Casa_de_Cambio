package pe.edu.utp.casacambio.modelo.seguridad;

import jakarta.persistence.*;
import lombok.*;

/**
 * Accion especifica que un rol puede realizar en el sistema.
 *
 * Ejemplos: CREAR_TRANSACCION, VER_REPORTES, MODIFICAR_TIPO_CAMBIO.
 * Cada permiso esta asignado a un rol y no puede existir sin el.
 *
 * Principio SOLID - ISP: permisos granulares evitan implementar
 * acciones innecesarias en cada rol.
 */
@Entity
@Table(name = "permisos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class Permiso {

    /** Identificador unico del permiso. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del permiso en formato constante (ej: CREAR_TRANSACCION). */
    @Column(nullable = false, unique = true)
    private String nombre;

    /** Descripcion de la accion que habilita este permiso. */
    private String descripcion;

    /** Rol al que pertenece este permiso. */
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;
}
