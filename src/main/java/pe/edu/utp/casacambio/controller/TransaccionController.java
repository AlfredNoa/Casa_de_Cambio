package pe.edu.utp.casacambio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.casacambio.dto.request.TransaccionRequestDTO;
import pe.edu.utp.casacambio.dto.response.TransaccionResponseDTO;
import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.cliente.ParMoneda;
import pe.edu.utp.casacambio.modelo.transaccion.Transaccion;
import pe.edu.utp.casacambio.service.cliente.ClienteService;
import pe.edu.utp.casacambio.service.transaccion.TransaccionService;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;
    private final ClienteService clienteService;
    private final ParMonedaResolver parMonedaResolver;

    public TransaccionController(TransaccionService transaccionService, ClienteService clienteService,
                                  ParMonedaResolver parMonedaResolver) {
        this.transaccionService = transaccionService;
        this.clienteService = clienteService;
        this.parMonedaResolver = parMonedaResolver;
    }

    @PostMapping
    public ResponseEntity<TransaccionResponseDTO> crear(@RequestBody TransaccionRequestDTO requestDTO) {
        Cliente cliente = clienteService.buscarPorId(requestDTO.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        // Resolvemos un ParMoneda YA PERSISTIDO (busca o crea), para que
        // Hibernate pueda guardar la Transaccion sin fallar por referenciar
        // una instancia transitoria no guardada.
        ParMoneda parMoneda = parMonedaResolver.resolver(requestDTO.getMonedaOrigen(), requestDTO.getMonedaDestino());

        Transaccion transaccion = transaccionService.crear(
                requestDTO.getTipoOperacion(),
                cliente,
                parMoneda,
                requestDTO.getMontoEntregado()
        );

        return new ResponseEntity<>(mapToResponseDTO(transaccion), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<TransaccionResponseDTO> confirmar(@PathVariable Long id) {
        Transaccion transaccion = transaccionService.confirmar(id);
        return ResponseEntity.ok(mapToResponseDTO(transaccion));
    }

    @PostMapping("/{id}/anular")
    public ResponseEntity<TransaccionResponseDTO> anular(@PathVariable Long id) {
        Transaccion transaccion = transaccionService.anular(id);
        return ResponseEntity.ok(mapToResponseDTO(transaccion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> buscarPorId(@PathVariable Long id) {
        return transaccionService.buscarPorId(id)
                .map(t -> ResponseEntity.ok(mapToResponseDTO(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TransaccionResponseDTO>> listarTodas() {
        List<TransaccionResponseDTO> transacciones = transaccionService.listarTodas().stream()
                .map(this::mapToResponseDTO)
                .toList();
        return ResponseEntity.ok(transacciones);
    }

    private TransaccionResponseDTO mapToResponseDTO(Transaccion transaccion) {
        TransaccionResponseDTO dto = new TransaccionResponseDTO();
        dto.setId(transaccion.getId());
        dto.setTipoOperacion(transaccion.getTipo().name());
        dto.setParMoneda(transaccion.getParMoneda() != null ? transaccion.getParMoneda().getPar() : null);
        dto.setMontoEntregado(transaccion.getMontoEntregado());
        
        if (transaccion.getDetalle() != null) {
            dto.setMontoRecibido(transaccion.getDetalle().getMontoFinal());
            dto.setTipoCambioUsado(transaccion.getDetalle().getTipoCambioAplicado());
            dto.setComision(transaccion.getDetalle().getComision());
        }
        
        dto.setEstado(transaccion.getEstado());
        return dto;
    }
}
