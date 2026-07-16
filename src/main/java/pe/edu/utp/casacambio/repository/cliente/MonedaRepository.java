package pe.edu.utp.casacambio.repository.cliente;

import pe.edu.utp.casacambio.modelo.cliente.Moneda;
import java.util.Optional;

/**
 * Contrato de persistencia para Moneda.
 * Principio SOLID - DIP: los consumidores dependen de esta interfaz,
 * no de la implementacion concreta con EntityManager.
 */
public interface MonedaRepository {

    Moneda guardar(Moneda moneda);

    Optional<Moneda> buscarPorCodigo(String codigo);
}
