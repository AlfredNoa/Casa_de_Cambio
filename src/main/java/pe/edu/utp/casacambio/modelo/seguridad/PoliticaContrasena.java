package pe.edu.utp.casacambio.modelo.seguridad;

import jakarta.persistence.*;
import lombok.*;

/**
 * Define las reglas que debe cumplir una contrasena en el sistema.
 *
 * Permite configurar longitud minima, requerimiento de mayusculas,
 * numeros y caracteres especiales, ademas del tiempo de vigencia.
 *
 * Principio SOLID - SRP: unica responsabilidad es validar contrasenas.
 */
@Entity
@Table(name = "politicas_contrasena")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class PoliticaContrasena {

    /** Identificador unico de la politica. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cantidad minima de caracteres requeridos. */
    @Column(nullable = false)
    private int longitudMinima;

    /** Si es true, la contrasena debe contener al menos una letra mayuscula. */
    private boolean requiereMayuscula;

    /** Si es true, la contrasena debe contener al menos un numero. */
    private boolean requiereNumero;

    /** Si es true, la contrasena debe contener al menos un caracter especial (!@#$%^&*). */
    private boolean requiereCaracterEspecial;

    /** Numero de dias que la contrasena es valida antes de expirar. */
    private int diasVigencia;

    /** Caracteres especiales aceptados por la politica. */
    private static final String CARACTERES_ESPECIALES = "!@#$%^&*";

    /**
     * Valida si una contrasena cumple con todas las reglas de esta politica.
     *
     * Se evita el uso de expresiones regulares tipo ".*X.*" (SonarQube java:S5852,
     * vulnerables a ReDoS por backtracking) y en su lugar se recorre la cadena
     * caracter por caracter, ademas de resolverse en un unico return (java:S1126).
     *
     * @param contrasena contrasena a evaluar
     * @return true si cumple todas las reglas, false en caso contrario
     */
    public boolean validar(String contrasena) {
        if (contrasena == null || contrasena.length() < longitudMinima) {
            return false;
        }
        boolean cumpleMayuscula = !requiereMayuscula || tieneMayuscula(contrasena);
        boolean cumpleNumero = !requiereNumero || tieneDigito(contrasena);
        boolean cumpleCaracterEspecial = !requiereCaracterEspecial || tieneCaracterEspecial(contrasena);
        return cumpleMayuscula && cumpleNumero && cumpleCaracterEspecial;
    }

    /**
     * Verifica si la cadena contiene al menos una letra mayuscula.
     */
    private boolean tieneMayuscula(String texto) {
        return texto.chars().anyMatch(Character::isUpperCase);
    }

    /**
     * Verifica si la cadena contiene al menos un digito.
     */
    private boolean tieneDigito(String texto) {
        return texto.chars().anyMatch(Character::isDigit);
    }

    /**
     * Verifica si la cadena contiene al menos uno de los caracteres especiales permitidos.
     */
    private boolean tieneCaracterEspecial(String texto) {
        return texto.chars().anyMatch(c -> CARACTERES_ESPECIALES.indexOf(c) >= 0);
    }
}
