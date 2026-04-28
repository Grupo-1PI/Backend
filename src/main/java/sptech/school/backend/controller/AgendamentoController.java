package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.AgendamentoRequestDto;
import sptech.school.backend.dto.AgendamentoResponseDto;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.mapper.AgendamentoMapper;
import sptech.school.backend.service.AgendamentoService;

import java.util.List;
@Tag(name = "Agendamentos", description = "Operações relacionados aos agendamentos")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @Operation(summary = "Criar novo agendamento")
    @ApiResponse(responseCode = "201")
    @PostMapping
    public ResponseEntity<AgendamentoResponseDto> criar(@RequestBody AgendamentoRequestDto dto) {

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(dto.getDataHora());

        Agendamento salvo = service.criar(agendamento, dto.getClienteId());

        return ResponseEntity.status(201).body(AgendamentoMapper.toResponse(salvo));
    }

    @Operation(summary = "Listar agendamentos")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDto>> listar() {

        List<AgendamentoResponseDto> lista = service.listar()
                .stream()
                .map(AgendamentoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Atualizar agendamento por ID")
    @ApiResponse(responseCode = "200")
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> atualizar(
            @Parameter(
                    name = "id",
                    description = "Identificador único do agendamento",
                    schema = @Schema(type = "string", format = "ID numérico", example = "1")
            )
            @PathVariable Long id,
            @RequestBody AgendamentoRequestDto dto) {

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(dto.getDataHora());

        Agendamento atualizado = service.atualizar(id, agendamento, dto.getClienteId());

        return ResponseEntity.ok(AgendamentoMapper.toResponse(atualizado));
    }

    @Operation(summary = "Apagar agendamento por ID")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(
                    name = "id",
                    description = "Identificador único do agendamento",
                    schema = @Schema(type = "string", format = "ID numérico", example = "1")
            )
            @PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
