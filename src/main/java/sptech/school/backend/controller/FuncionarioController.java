package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.FuncionarioDto.FuncionarioCriacaoDto;
import sptech.school.backend.dto.FuncionarioDto.FuncionarioResponseDto;
import sptech.school.backend.mapper.FuncionarioMapper;
import sptech.school.backend.service.FuncionarioService;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Funcionários", description = "Operações relacionadas aos funcionários da clínica")
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @Operation(summary = "Criar novo funcionário")
    @ApiResponse(responseCode = "201", description = "Funcionário criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário ou cargo não encontrado")
    @PostMapping
    public ResponseEntity<FuncionarioResponseDto> criar(@RequestBody FuncionarioCriacaoDto dto) {

        return ResponseEntity.status(201).body(
                FuncionarioMapper.toDto(
                        service.criar(dto.getUsuarioId(), dto.getCargoId())
                )
        );
    }

    @Operation(summary = "Listar todos os funcionários")
    @ApiResponse(responseCode = "200", description = "Lista de funcionários retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDto>> listar() {

        List<FuncionarioResponseDto> lista = service.listar()
                .stream()
                .map(FuncionarioMapper::toDto)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Atualizar cargo do funcionário")
    @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionário ou cargo não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDto> atualizar(

            @Parameter(description = "ID do funcionário", example = "1")
            @PathVariable Long id,

            @RequestBody FuncionarioCriacaoDto dto
    ) {

        return ResponseEntity.ok(
                FuncionarioMapper.toDto(
                        service.atualizar(id, dto.getCargoId())
                )
        );
    }

    @Operation(summary = "Deletar funcionário")
    @ApiResponse(responseCode = "204", description = "Funcionário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do funcionário", example = "1")
            @PathVariable Long id
    ) {

        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}