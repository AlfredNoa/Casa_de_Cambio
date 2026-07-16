package pe.edu.utp.casacambio.service.patron.singleton;

import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;
import lombok.extern.slf4j.Slf4j;
import java.time.Clock;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestor centralizado de tipos de cambio vigentes del dia.
 * Patron: Singleton. Principio SOLID: SRP.
 */
@Slf4j
public class GestorTipoCambio {

    /** Unica instancia estatica — nucleo del patron Singleton. */
    private static GestorTipoCambio instancia;

    /** Tipos de cambio indexados por par de monedas (clave: "PEN/USD"). */
    private final Map<String, TipoCambio> tiposCambioVigentes;

    /**
     * Constructor privado: impide que cualquier clase externa cree
     * instancias con "new GestorTipoCambio()".
     */
    private GestorTipoCambio() {
        this.tiposCambioVigentes = new HashMap<>();
    }

    /**
     * Punto de acceso global a la unica instancia del gestor.
     * Usa lazy initialization y synchronized para thread-safety en Spring Boot.
     *
     * @return unica instancia de GestorTipoCambio
     */
    public static synchronized GestorTipoCambio getInstancia() {
        // Si no existe instancia, se crea por primera vez (lazy init)
        if (instancia == null) {
            instancia = new GestorTipoCambio();
            log.info("GestorTipoCambio: instancia creada.");
        }
        // Siempre retornamos la misma instancia
        return instancia;
    }

    /**
     * Registra o sobreescribe el tipo de cambio vigente para un par de monedas.
     *
     * @param tipoCambio tipo de cambio a registrar
     */
    public void actualizarTipoCambio(TipoCambio tipoCambio) {
        // Obtenemos la clave en formato "PEN/USD"
        String clave = tipoCambio.getParMoneda().getPar();
        // Guardamos o sobreescribimos el valor en el mapa
        tiposCambioVigentes.put(clave, tipoCambio);
        log.info("Tipo de cambio actualizado: {} | Compra: {} | Venta: {}",
                clave, tipoCambio.getPrecioCompra(), tipoCambio.getPrecioVenta());
    }

    /**
     * Retorna el tipo de cambio vigente para el par indicado, usando el
     * reloj real del sistema para determinar "hoy".
     *
     * @param parMoneda par de monedas a consultar
     * @return tipo de cambio vigente, o null si no existe o esta desactualizado
     */
    public TipoCambio obtenerTipoCambio(ParMoneda parMoneda) {
        return obtenerTipoCambio(parMoneda, Clock.system(ZoneId.of("America/Lima")));
    }

    /**
     * Retorna el tipo de cambio vigente para el par indicado segun el
     * reloj proporcionado. Si el tipo de cambio registrado no corresponde
     * al dia de hoy segun ese reloj (ver {@link TipoCambio#esVigente(Clock)}),
     * se considera desactualizado y no se retorna. Sobrecarga inyectable
     * para permitir pruebas unitarias deterministicas (SonarQube S8692).
     *
     * @param parMoneda par de monedas a consultar
     * @param clock reloj a partir del cual se determina "hoy"
     * @return tipo de cambio vigente, o null si no existe o esta desactualizado
     */
    public TipoCambio obtenerTipoCambio(ParMoneda parMoneda, Clock clock) {
        // Buscamos por la clave "PEN/USD" generada por getPar()
        TipoCambio tipoCambio = tiposCambioVigentes.get(parMoneda.getPar());
        if (tipoCambio != null && !tipoCambio.esVigente(clock)) {
            log.warn("Tipo de cambio para {} esta desactualizado (no es de hoy).", parMoneda.getPar());
            return null;
        }
        return tipoCambio;
    }

    /**
     * Verifica si existe tipo de cambio vigente (registrado y del dia de
     * hoy) para el par indicado, usando el reloj real del sistema.
     *
     * @param parMoneda par de monedas a verificar
     * @return true si existe y esta vigente, false en caso contrario
     */
    public boolean existeTipoCambio(ParMoneda parMoneda) {
        return obtenerTipoCambio(parMoneda) != null;
    }

    /**
     * Verifica si existe tipo de cambio vigente segun el reloj indicado.
     * Sobrecarga inyectable para pruebas unitarias deterministicas.
     *
     * @param parMoneda par de monedas a verificar
     * @param clock reloj a partir del cual se determina "hoy"
     * @return true si existe y esta vigente, false en caso contrario
     */
    public boolean existeTipoCambio(ParMoneda parMoneda, Clock clock) {
        return obtenerTipoCambio(parMoneda, clock) != null;
    }

    /**
     * Elimina todos los tipos de cambio vigentes al cierre del dia.
     */
    public void limpiarTiposCambio() {
        // Limpiamos el mapa para el siguiente dia de operaciones
        tiposCambioVigentes.clear();
        log.info("Tipos de cambio del dia limpiados.");
    }
}
