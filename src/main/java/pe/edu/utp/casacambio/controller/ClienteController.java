package pe.edu.utp.casacambio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.casacambio.dto.request.ClienteRequestDTO;
import pe.edu.utp.casacambio.dto.response.ClienteResponseDTO;
import pe.edu.utp.casacambio.modelo.cliente.Cliente;
import pe.edu.utp.casacambio.modelo.cliente.TipoCliente;
import pe.edu.utp.casacambio.service.cliente.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrar(@RequestBody ClienteRequestDTO requestDTO) {
        Cliente cliente = new Cliente();
        cliente.setNombre(requestDTO.getNombre());
        cliente.setDni(requestDTO.getDni());
        cliente.setEmail(requestDTO.getEmail());
        cliente.setTelefono(requestDTO.getTelefono());
        // Si no se especifica tipo de cliente, se asume NATURAL por defecto
        cliente.setTipoCliente(requestDTO.getTipoCliente() != null
                ? requestDTO.getTipoCliente()
                : TipoCliente.NATURAL);

        Cliente clienteGuardado = clienteService.registrar(cliente);
        return new ResponseEntity<>(mapToResponseDTO(clienteGuardado), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(cliente -> ResponseEntity.ok(mapToResponseDTO(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> clientes = clienteService.listarTodos().stream()
                .map(this::mapToResponseDTO)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    private ClienteResponseDTO mapToResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setDni(cliente.getDni());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setTipoCliente(cliente.getTipoCliente());
        return dto;
    }
}
