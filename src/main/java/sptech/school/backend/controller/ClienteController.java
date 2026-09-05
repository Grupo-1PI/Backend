package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Clientes", description = "Consulta de clientes cadastrados")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("!hasAuthority('CLIENTE')")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Listar clientes", description = "Retorna todos os clientes cadastrados.")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listar() {
        return ResponseEntity.ok(
                clienteService.listar()
                        .stream()
                        .map(ClienteMapper::toResponse)
                        .toList()
        );
    }

    @Operation(summary = "Buscar cliente por ID", description = "Retorna os dados de um cliente especifico.")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ClienteMapper.toResponse(clienteService.buscarPorId(id)));
    }
}
