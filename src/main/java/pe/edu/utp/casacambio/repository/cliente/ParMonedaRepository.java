package pe.edu.utp.casacambio.repository.cliente;

import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import java.util.Optional;

/**
 * Contrato de persistencia para ParMoneda.
 * Principio SOLID - DIP: los consumidores dependen de esta interfaz,
 * no de la implementacion concreta con EntityManager.
 */
public interface ParMonedaRepository {

    ParMoneda guardar(ParMoneda parMoneda);

    Optional<ParMoneda> buscarPorCodigos(String codigoOrigen, String codigoDestino);
}
