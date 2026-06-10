package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.EspecialidadeDto.EspecialidadeCriacaoDto;
import sptech.school.backend.dto.EspecialidadeDto.EspecialidadeResponseDto;
import sptech.school.backend.mapper.EspecialidadeMapper;
import sptech.school.backend.service.EspecialidadeService;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")

@Tag(name = "Administracao - Especialidades", description = "Cadastro e manutencao de especialidades e servicos vinculados")
@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    private final EspecialidadeService service;

    public EspecialidadeController(EspecialidadeService service) {
        this.service = service;
    }

    @Operation(summary = "Listar especialidades", description = "Retorna todas as especialidades cadastradas com seus servicos.")
    @ApiResponse(responseCode = "200", description = "Lista de especialidades retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<EspecialidadeResponseDto>> listar() {
        List<EspecialidadeResponseDto> lista = service.listar()
                .stream()
                .map(EspecialidadeMapper::toResponse)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar especialidade por ID", description = "Retorna uma especialidade especifica pelo ID.")
    @ApiResponse(responseCode = "200", description = "Especialidade retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Especialidade nao encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(EspecialidadeMapper.toResponse(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar especialidade", description = "Cria uma especialidade e vincula os servicos informados.")
    @ApiResponse(responseCode = "201", description = "Especialidade criada com sucesso")
    @ApiResponse(responseCode = "404", description = "Servico nao encontrado")
    @PostMapping
    public ResponseEntity<EspecialidadeResponseDto> criar(@Valid @RequestBody EspecialidadeCriacaoDto dto) {
        return ResponseEntity.status(201).body(EspecialidadeMapper.toResponse(service.criar(dto)));
    }

    @Operation(summary = "Atualizar especialidade", description = "Atualiza dados e servicos vinculados a uma especialidade.")
    @ApiResponse(responseCode = "200", description = "Especialidade atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Especialidade ou servico nao encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EspecialidadeCriacaoDto dto
    ) {
        return ResponseEntity.ok(EspecialidadeMapper.toResponse(service.atualizar(id, dto)));
    }

    @Operation(summary = "Excluir especialidade", description = "Remove uma especialidade existente pelo ID.")
    @ApiResponse(responseCode = "204", description = "Especialidade deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Especialidade nao encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
