package pe.edu.utp.casacambio.controller;

import org.springframework.stereotype.Component;
import pe.edu.utp.casacambio.modelo.cliente.Moneda;
import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import pe.edu.utp.casacambio.repository.cliente.MonedaRepository;
import pe.edu.utp.casacambio.repository.cliente.ParMonedaRepository;

import java.util.Map;

/**
 * Resuelve un ParMoneda ya persistido a partir de dos codigos de moneda
 * (ej: "PEN", "USD"). Se usa en los Controllers para no exponer la
 * entidad JPA completa en los DTOs de entrada (ver TransaccionRequestDTO).
 *
 * Busca primero si el par y las monedas ya existen en base de datos;
 * si no existen, las crea. Esto evita el error de Hibernate
 * "references an unsaved transient instance" al guardar una Transaccion
 * que referencia un ParMoneda transitorio, y evita crear filas
 * duplicadas de Moneda/ParMoneda en cada request.
 */
@Component
public class ParMonedaResolver {

    private static final Map<String, String[]> MONEDAS_CONOCIDAS = Map.of(
            "PEN", new String[]{"Sol peruano", "S/"},
            "USD", new String[]{"Dólar americano", "$"},
            "EUR", new String[]{"Euro", "€"}
    );

    private final MonedaRepository monedaRepository;
    private final ParMonedaRepository parMonedaRepository;

    public ParMonedaResolver(MonedaRepository monedaRepository, ParMonedaRepository parMonedaRepository) {
        this.monedaRepository = monedaRepository;
        this.parMonedaRepository = parMonedaRepository;
    }

    /**
     * Busca el ParMoneda persistido para el par de codigos indicado; si
     * no existe, crea (y persiste) las Monedas y el ParMoneda necesarios.
     *
     * @param codigoOrigen codigo ISO de la moneda de origen, ej: "PEN"
     * @param codigoDestino codigo ISO de la moneda de destino, ej: "USD"
     * @return ParMoneda ya persistido (con id asignado)
     */
    public ParMoneda resolver(String codigoOrigen, String codigoDestino) {
        return parMonedaRepository.buscarPorCodigos(codigoOrigen, codigoDestino)
                .orElseGet(() -> {
                    Moneda origen = resolverMoneda(codigoOrigen);
                    Moneda destino = resolverMoneda(codigoDestino);
                    ParMoneda nuevo = new ParMoneda();
                    nuevo.setMonedaOrigen(origen);
                    nuevo.setMonedaDestino(destino);
                    nuevo.setActivo(true);
                    return parMonedaRepository.guardar(nuevo);
                });
    }

    private Moneda resolverMoneda(String codigo) {
        return monedaRepository.buscarPorCodigo(codigo)
                .orElseGet(() -> {
                    String[] datos = MONEDAS_CONOCIDAS.getOrDefault(codigo, new String[]{codigo, codigo});
                    Moneda nueva = new Moneda();
                    nueva.setCodigo(codigo);
                    nueva.setNombre(datos[0]);
                    nueva.setSimbolo(datos[1]);
                    return monedaRepository.guardar(nueva);
                });
    }
}
