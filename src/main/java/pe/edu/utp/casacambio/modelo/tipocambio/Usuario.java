package pe.edu.utp.casacambio.modelo.tipocambio;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa a un operador del sistema: cajero, supervisor o administrador.
 *
 * Cada usuario pertenece a una sucursal y tiene un rol que determina
 * los permisos que puede ejercer dentro del sistema.
 *
 * Principio SOLID - SRP: solo gestiona los datos del usuario.
 * La autenticacion completa reside en UsuarioService.
 */
@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "password")
public class Usuario {

    /** Identificador unico del usuario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de usuario para el ingreso al sistema. Unico. */
    @Column(nullable = false, unique = true)
    private String username;

    /** Contrasena del usuario. Excluida del toString por seguridad. */
    @Column(nullable = false)
    private String password;

    /** Rol del usuario en el sistema: CAJERO, SUPERVISOR o ADMIN. */
    @Column(nullable = false)
    private String rol;

    /** Sucursal a la que esta asignado este usuario. */
    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    /**
     * Verifica si la contrasena ingresada coincide con la registrada.
     *
     * @param passwordIngresada contrasena a verificar
     * @return true si la autenticacion es exitosa, false en caso contrario
     */
    public boolean autenticar(String passwordIngresada) {
        return this.password.equals(passwordIngresada);
    }
}
