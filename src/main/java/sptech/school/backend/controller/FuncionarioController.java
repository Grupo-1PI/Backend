package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.backend.dto.FuncionarioDto.FuncionarioAtualizacaoDto;
import sptech.school.backend.dto.FuncionarioDto.FuncionarioCriacaoDto;
import sptech.school.backend.dto.FuncionarioDto.FuncionarioResponseDto;
import sptech.school.backend.mapper.FuncionarioMapper;
import sptech.school.backend.service.FuncionarioService;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Funcionarios", description = "Operacoes relacionadas aos funcionarios da clinica")
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os funcionarios")
    @ApiResponse(responseCode = "200", description = "Lista de funcionarios retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDto>> listar() {
        List<FuncionarioResponseDto> lista = service.listar()
                .stream()
                .map(FuncionarioMapper::toResponse)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar funcionario por id")
    @ApiResponse(responseCode = "200", description = "Funcionario encontrado")
    @ApiResponse(responseCode = "404", description = "Funcionario nao encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDto> buscarPorId(
            @Parameter(description = "ID do funcionario", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(FuncionarioMapper.toResponse(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar novo funcionario")
    @ApiResponse(responseCode = "201", description = "Funcionario criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cargo ou especialidade nao encontrada")
    @PostMapping
    public ResponseEntity<FuncionarioResponseDto> criar(@Valid @RequestBody FuncionarioCriacaoDto dto) {
        return ResponseEntity.status(201).body(FuncionarioMapper.toResponse(service.criar(dto)));
    }

    @Operation(summary = "Atualizar funcionario")
    @ApiResponse(responseCode = "200", description = "Funcionario atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionario, cargo ou especialidade nao encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDto> atualizar(
            @Parameter(description = "ID do funcionario", example = "1")
            @PathVariable Long id,
            @RequestBody FuncionarioAtualizacaoDto dto
    ) {
        return ResponseEntity.ok(FuncionarioMapper.toResponse(service.atualizar(id, dto)));
    }

    @Operation(summary = "Deletar funcionario")
    @ApiResponse(responseCode = "204", description = "Funcionario deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionario nao encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do funcionario", example = "1")
            @PathVariable Long id
    ) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
