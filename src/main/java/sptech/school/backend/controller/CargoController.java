package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.CargoDto.CargoCriacaoDto;
import sptech.school.backend.dto.CargoDto.CargoResponseDto;
import sptech.school.backend.mapper.CargoMapper;
import sptech.school.backend.service.CargoService;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")

@Tag(name = "Administracao - Cargos", description = "Cadastro e manutencao de cargos e permissoes")
@RestController
@RequestMapping("/cargos")
public class CargoController {

    private final CargoService service;

    public CargoController(CargoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar cargos", description = "Retorna todos os cargos cadastrados com suas permissoes.")
    @ApiResponse(responseCode = "200", description = "Lista de cargos retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<CargoResponseDto>> listar() {

        List<CargoResponseDto> lista = service.listar()
                .stream()
                .map(CargoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar cargo por ID", description = "Retorna um cargo especifico pelo ID.")
    @ApiResponse(responseCode = "200", description = "Cargo retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cargo não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<CargoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(CargoMapper.toResponse(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar cargo", description = "Cria um cargo e vincula as permissoes informadas.")
    @ApiResponse(responseCode = "201", description = "Cargo criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Permissão não encontrada")
    @PostMapping
    public ResponseEntity<CargoResponseDto> criar(@Valid @RequestBody CargoCriacaoDto dto) {
        return ResponseEntity.status(201).body(CargoMapper.toResponse(service.criar(dto)));
    }

    @Operation(summary = "Atualizar cargo", description = "Atualiza dados e permissoes vinculadas a um cargo.")
    @ApiResponse(responseCode = "200", description = "Cargo atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cargo ou permissão não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<CargoResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CargoCriacaoDto dto
    ) {
        return ResponseEntity.ok(CargoMapper.toResponse(service.atualizar(id, dto)));
    }

    @Operation(summary = "Excluir cargo", description = "Remove um cargo existente pelo ID.")
    @ApiResponse(responseCode = "204", description = "Cargo deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cargo não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
