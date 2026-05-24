package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.CargoDto.CargoResponseDto;
import sptech.school.backend.mapper.CargoMapper;
import sptech.school.backend.service.CargoService;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cargos", description = "Operações relacionadas aos cargos")
@RestController
@RequestMapping("/cargos")
public class CargoController {

    private final CargoService service;

    public CargoController(CargoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os cargos")
    @ApiResponse(responseCode = "200", description = "Lista de cargos retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<CargoResponseDto>> listar() {

        List<CargoResponseDto> lista = service.listar()
                .stream()
                .map(CargoMapper::toDto)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar cargo por ID")
    @ApiResponse(responseCode = "200", description = "Cargo retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cargo não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<CargoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(CargoMapper.toDto(service.buscarPorId(id)));
    }
}
