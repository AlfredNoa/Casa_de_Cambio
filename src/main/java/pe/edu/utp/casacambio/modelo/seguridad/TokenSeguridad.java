package pe.edu.utp.casacambio.modelo.seguridad;

import pe.edu.utp.casacambio.modelo.tipocambio.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Token de sesion que valida que un usuario esta autenticado.
 *
 * El token tiene una fecha de expiracion y puede invalidarse
 * manualmente al cerrar sesion. Se excluye del toString por seguridad.
 *
 * Principio SOLID - SRP: solo gestiona la validez del token de sesion.
 */
@Entity
@Table(name = "tokens_seguridad")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "valor")
public class TokenSeguridad {

    /** Identificador unico del token. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Valor del token. Excluido del toString por razones de seguridad. */
    @Column(nullable = false, unique = true)
    private String valor;

    /** Usuario al que pertenece este token. */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Fecha y hora en que el token deja de ser valido. */
    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    /** Indica si el token sigue siendo valido (no fue invalidado manualmente). */
    @Column(nullable = false)
    private boolean vigente;

    /**
     * Verifica si el token es valido: debe estar vigente y no haber expirado,
     * usando la zona horaria de Peru y el reloj del sistema.
     *
     * @return true si el token es valido, false en caso contrario
     */
    public boolean esValido() {
        return esValido(Clock.system(ZoneId.of("America/Lima")));
    }

    /**
     * Verifica si el token es valido segun el reloj indicado. Se expone con
     * Clock inyectable (en vez de usar LocalDateTime.now() directamente) para
     * permitir pruebas unitarias deterministicas sin depender del reloj real
     * del sistema (SonarQube S8692).
     *
     * @param clock reloj a partir del cual se evalua la expiracion
     * @return true si el token es valido segun el reloj dado
     */
    public boolean esValido(Clock clock) {
        return vigente && LocalDateTime.now(clock).isBefore(fechaExpiracion);
    }

    /**
     * Invalida el token manualmente, por ejemplo al cerrar sesion.
     */
    public void invalidar() {
        this.vigente = false;
    }
}
