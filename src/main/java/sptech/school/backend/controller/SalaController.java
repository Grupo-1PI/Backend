package sptech.school.backend.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.SalaDto.SalaCriacaoDto;
import sptech.school.backend.dto.SalaDto.SalaResponseDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.service.SalaService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/salas")
@Tag(name = "5d. Gerenciamento — Salas")
@SecurityRequirement(name = "bearerAuth")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping
    public ResponseEntity<List<SalaResponseDto>> listar() {

        List<SalaResponseDto> response = salaService.listar()
                .stream()
                .map(this::toResponseDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaResponseDto> buscarPorId(
            @PathVariable Long id) {

        Sala sala = salaService.buscarPorId(id);

        return ResponseEntity.ok(toResponseDto(sala));
    }

    @PostMapping
    public ResponseEntity<SalaResponseDto> criar(
            @RequestBody SalaCriacaoDto dto) {

        Sala sala = salaService.criar(dto);

        SalaResponseDto response = toResponseDto(sala);

        return ResponseEntity
                .created(URI.create("/salas/" + sala.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody SalaCriacaoDto dto) {

        Sala sala = salaService.atualizar(id, dto);

        return ResponseEntity.ok(toResponseDto(sala));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        salaService.deletar(id);

        return ResponseEntity.noContent().build();
    }

    private SalaResponseDto toResponseDto(Sala sala) {
        return new SalaResponseDto(
                sala.getId(),
                sala.getDescricao()
        );
    }

}
