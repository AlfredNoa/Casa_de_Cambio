package pe.edu.utp.casacambio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.casacambio.dto.request.TipoCambioRequestDTO;
import pe.edu.utp.casacambio.dto.response.TipoCambioResponseDTO;
import pe.edu.utp.casacambio.modelo.cliente.Moneda;
import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import pe.edu.utp.casacambio.modelo.tipocambio.TipoCambio;
import pe.edu.utp.casacambio.service.patron.singleton.GestorTipoCambio;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Controller REST para consultar y actualizar el tipo de cambio vigente.
 * Delega en el Singleton GestorTipoCambio, que es quien realmente
 * mantiene los tipos de cambio del dia en memoria.
 *
 * Nota: monedaOrigen/monedaDestino se reciben como codigos simples
 * (ej: "PEN", "USD") en vez de la entidad ParMoneda completa, ya que
 * Spring no puede convertir un @RequestParam directamente en una
 * entidad JPA sin un Converter personalizado.
 */
@RestController
@RequestMapping("/api/tipocambio")
public class TipoCambioController {

    @PostMapping
    public ResponseEntity<TipoCambioResponseDTO> actualizarTipoCambio(@RequestBody TipoCambioRequestDTO requestDTO) {
        TipoCambio tipoCambio = new TipoCambio();
        tipoCambio.setParMoneda(construirParMoneda(requestDTO.getMonedaOrigen(), requestDTO.getMonedaDestino()));
        tipoCambio.setPrecioCompra(requestDTO.getPrecioCompra());
        tipoCambio.setPrecioVenta(requestDTO.getPrecioVenta());
        tipoCambio.setFechaHora(LocalDateTime.now(ZoneId.of("America/Lima")));

        GestorTipoCambio.getInstancia().actualizarTipoCambio(tipoCambio);
        return ResponseEntity.ok(mapToResponseDTO(tipoCambio));
    }

    @GetMapping
    public ResponseEntity<TipoCambioResponseDTO> obtenerTipoCambio(
            @RequestParam String monedaOrigen,
            @RequestParam String monedaDestino) {
        ParMoneda parMoneda = construirParMoneda(monedaOrigen, monedaDestino);
        TipoCambio tipoCambio = GestorTipoCambio.getInstancia().obtenerTipoCambio(parMoneda);

        if (tipoCambio != null) {
            return ResponseEntity.ok(mapToResponseDTO(tipoCambio));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> limpiarTiposCambio() {
        GestorTipoCambio.getInstancia().limpiarTiposCambio();
        return ResponseEntity.ok().build();
    }

    /**
     * Arma un ParMoneda liviano a partir de dos codigos de moneda. Se usa
     * solo para consultar/indexar en el GestorTipoCambio (que guarda
     * en memoria por el String de getPar()), no para persistir en BD.
     */
    private ParMoneda construirParMoneda(String codigoOrigen, String codigoDestino) {
        Moneda monedaOrigen = new Moneda();
        monedaOrigen.setCodigo(codigoOrigen);

        Moneda monedaDestino = new Moneda();
        monedaDestino.setCodigo(codigoDestino);

        ParMoneda parMoneda = new ParMoneda();
        parMoneda.setMonedaOrigen(monedaOrigen);
        parMoneda.setMonedaDestino(monedaDestino);
        parMoneda.setActivo(true);
        return parMoneda;
    }

    private TipoCambioResponseDTO mapToResponseDTO(TipoCambio tipoCambio) {
        TipoCambioResponseDTO dto = new TipoCambioResponseDTO();
        dto.setParMoneda(tipoCambio.getParMoneda().getPar());
        dto.setPrecioCompra(tipoCambio.getPrecioCompra());
        dto.setPrecioVenta(tipoCambio.getPrecioVenta());
        dto.setFechaRegistro(tipoCambio.getFechaHora().toLocalDate());
        return dto;
    }
}
