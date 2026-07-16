package pe.edu.utp.casacambio.modelo.notificacion;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Parametros globales configurables del sistema de casa de cambio.
 *
 * Define el nombre de la empresa, la moneda base, el horario de
 * atencion y si las notificaciones automaticas estan habilitadas.
 *
 * Solo debe existir una configuracion activa a la vez en el sistema.
 * Principio SOLID - SRP: unica responsabilidad es centralizar la configuracion.
 */
@Entity
@Table(name = "configuracion_sistema")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class ConfiguracionSistema {

    /** Identificador unico de la configuracion. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre comercial de la empresa casa de cambio. */
    @Column(nullable = false)
    private String nombreEmpresa;

    /** Codigo de la moneda base del sistema (PEN por defecto). */
    @Column(nullable = false)
    private String monedaBase;

    /** Hora de apertura de operaciones (ej: 9 para las 9:00 AM). */
    @Column(nullable = false)
    private int horaApertura;

    /** Hora de cierre de operaciones (ej: 18 para las 6:00 PM). */
    @Column(nullable = false)
    private int horaCierre;

    /** Indica si el sistema debe emitir notificaciones automaticas. */
    private boolean notificacionesActivas;

    /**
     * Verifica si la hora actual esta dentro del horario de atencion configurado.
     *
     * @return true si el sistema esta en horario de atencion
     */
    public boolean enHorarioAtencion() {
        int horaActual = LocalTime.now(ZoneId.of("America/Lima")).getHour();
        return horaActual >= horaApertura && horaActual < horaCierre;
    }
}
