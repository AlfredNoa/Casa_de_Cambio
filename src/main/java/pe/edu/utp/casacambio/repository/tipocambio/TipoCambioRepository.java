package pe.edu.utp.casacambio.repository.tipocambio;

import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de acceso a datos para la entidad TipoCambio.
 * Principio SOLID - DIP: los servicios dependen de esta
 * interfaz, no de una implementacion concreta.
 */
public interface TipoCambioRepository {

    /**
     * Guarda o actualiza un tipo de cambio en el repositorio.
     *
     * @param tipoCambio tipo de cambio a persistir
     * @return tipo de cambio guardado con su id asignado
     */
    TipoCambio guardar(TipoCambio tipoCambio);

    /**
     * Busca un tipo de cambio por su identificador unico.
     *
     * @param id identificador del tipo de cambio
     * @return Optional con el tipo de cambio si existe, vacio si no
     */
    Optional<TipoCambio> buscarPorId(Long id);

    /**
     * Retorna todos los tipos de cambio registrados.
     *
     * @return lista de tipos de cambio
     */
    List<TipoCambio> listarTodos();
}
