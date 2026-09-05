package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.backend.dto.ServicoDto.ServicoCriacaoDto;
import sptech.school.backend.dto.ServicoDto.ServicoResponseDto;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.mapper.ServicoMapper;
import sptech.school.backend.service.ServicoService;

import java.net.URI;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Administracao - Servicos", description = "Cadastro e manutencao dos servicos oferecidos")
@RestController
@RequestMapping("/servicos")
@PreAuthorize("hasAnyAuthority('CRUD_AGENDAMENTO', 'REALIZAR_ATENDIMENTO')")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar servicos", description = "Retorna todos os servicos com dados completos e salas vinculadas.")
    @GetMapping
    public ResponseEntity<List<ServicoResponseDto>> listar() {
        return ResponseEntity.ok(
                service.listar()
                        .stream()
                        .map(ServicoMapper::toResponse)
                        .toList()
        );
    }

    @Operation(summary = "Buscar servico por ID", description = "Retorna um servico especifico com dados completos.")
    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ServicoMapper.toResponse(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar servico", description = "Cria um servico e vincula as salas informadas.")
    @PostMapping
    public ResponseEntity<ServicoResponseDto> criar(@Valid @RequestBody ServicoCriacaoDto dto) {
        Servico servico = service.criar(dto);

        return ResponseEntity
                .created(URI.create("/servicos/" + servico.getId()))
                .body(ServicoMapper.toResponse(servico));
    }

    @Operation(summary = "Atualizar servico", description = "Atualiza os dados e as salas vinculadas ao servico.")
    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicoCriacaoDto dto
    ) {
        return ResponseEntity.ok(ServicoMapper.toResponse(service.atualizar(id, dto)));
    }

    @Operation(summary = "Excluir servico", description = "Remove um servico existente pelo ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
