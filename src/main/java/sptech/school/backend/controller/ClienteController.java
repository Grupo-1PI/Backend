package sptech.school.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.backend.dto.ClienteDto.ClienteResponseDto;
import sptech.school.backend.mapper.ClienteMapper;
import sptech.school.backend.service.ClienteService;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listar() {
        return ResponseEntity.ok(
                clienteService.listar()
                        .stream()
                        .map(ClienteMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ClienteMapper.toResponse(clienteService.buscarPorId(id)));
    }
}
