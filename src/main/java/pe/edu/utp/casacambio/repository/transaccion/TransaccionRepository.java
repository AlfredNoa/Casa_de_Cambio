package pe.edu.utp.casacambio.repository.transaccion;

import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de acceso a datos para la entidad Transaccion.
 * Principio SOLID - DIP: los servicios dependen de esta
 * interfaz, no de una implementacion concreta.
 */
public interface TransaccionRepository {

    /**
     * Guarda o actualiza una transaccion en el repositorio.
     *
     * @param transaccion transaccion a persistir
     * @return transaccion guardada con su id asignado
     */
    Transaccion guardar(Transaccion transaccion);

    /**
     * Busca una transaccion por su identificador unico.
     *
     * @param id identificador de la transaccion
     * @return Optional con la transaccion si existe, vacio si no
     */
    Optional<Transaccion> buscarPorId(Long id);

    /**
     * Retorna todas las transacciones registradas en el sistema.
     *
     * @return lista de transacciones, vacia si no hay ninguna
     */
    List<Transaccion> listarTodas();

    /**
     * Elimina una transaccion por su identificador.
     *
     * @param id identificador de la transaccion a eliminar
     */
    void eliminar(Long id);
}
