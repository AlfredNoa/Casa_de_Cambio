package pe.edu.utp.casacambio.modelo.tipocambio;

import pe.edu.utp.casacambio.modelo.cliente.TipoOperacion;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Criterios de busqueda para generar un reporte filtrado.
 *
 * Permite delimitar el reporte por rango de fechas, sucursal
 * y tipo de operacion. Se compone junto con Reporte.
 *
 * Relacion UML: composicion con Reporte (no tiene sentido sin el).
 */
@Entity
@Table(name = "filtros_reporte")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class FiltroReporte {

    /** Identificador unico del filtro. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha de inicio del periodo a reportar. */
    private LocalDate fechaInicio;

    /** Fecha de fin del periodo a reportar. */
    private LocalDate fechaFin;

    /** Sucursal sobre la que se generara el reporte. Opcional. */
    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    /** Tipo de operacion a filtrar. Opcional (null = todas). */
    @Enumerated(EnumType.STRING)
    private TipoOperacion tipoOperacion;

    /**
     * Aplica los criterios del filtro y retorna los resultados coincidentes.
     *
     * @return lista de objetos que coinciden con el filtro
     */
    public List<Object> aplicar() {
        return new ArrayList<>();
    }
}
