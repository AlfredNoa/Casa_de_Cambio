package pe.edu.utp.casacambio.modelo.seguridad;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Rol asignado a un usuario que determina sus permisos en el sistema.
 *
 * Los roles disponibles son: CAJERO, SUPERVISOR y ADMIN.
 * Cada rol agrega una lista de permisos especificos que puede ejercer.
 *
 * Principio SOLID - ISP: los permisos estan segregados por rol,
 * evitando que un usuario implemente acciones que no le corresponden.
 */
@Entity
@Table(name = "roles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "permisos")
public class Rol {

    /** Identificador unico del rol. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del rol. Unico en el sistema. */
    @Column(nullable = false, unique = true)
    private String nombre;

    /** Descripcion del alcance y responsabilidades del rol. */
    private String descripcion;

    /** Lista de permisos asignados a este rol (agregacion). */
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
    private List<Permiso> permisos = new ArrayList<>();

    /**
     * Retorna la lista de permisos asignados a este rol.
     *
     * @return lista de permisos
     */
    public List<Permiso> listarPermisos() {
        return permisos;
    }
}
