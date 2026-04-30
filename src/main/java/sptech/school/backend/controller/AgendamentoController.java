package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.AgendamentoDto.AgendamentoRequestDto;
import sptech.school.backend.dto.AgendamentoDto.AgendamentoResponseDto;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.mapper.AgendamentoMapper;
import sptech.school.backend.service.AgendamentoService;
import java.net.URI;
import java.util.List;

@Tag(name = "Agendamentos")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @Operation(summary = "Criar agendamento")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Regra de negócio inválida")
    @ApiResponse(responseCode = "404", description = "Recurso não encontrado")
    @ApiResponse(responseCode = "409", description = "Conflito de horário")
    @PostMapping
    public ResponseEntity<AgendamentoResponseDto> criar(@RequestBody AgendamentoRequestDto dto) {

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHoraInicio(dto.getDataHoraInicio());
        agendamento.setDataHoraFim(dto.getDataHoraFim());
        agendamento.setObservacao(dto.getObservacao());

        Agendamento salvo = service.criar(
                agendamento,
                dto.getClienteId(),
                dto.getFuncionarioId(),
                dto.getSalaId(),
                dto.getServicoId(),
                dto.getStatusId()
        );

        return ResponseEntity.created(URI.create("/agendamentos/" + salvo.getId()))
                .body(AgendamentoMapper.toResponse(salvo));
    }

    @Operation(summary = "Listar agendamentos")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDto>> listar() {

        List<AgendamentoResponseDto> lista = service.listar()
                .stream()
                .map(AgendamentoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Atualizar agendamento")
    @ApiResponse(responseCode = "200", description = "Atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Regra inválida")
    @ApiResponse(responseCode = "404", description = "Não encontrado")
    @ApiResponse(responseCode = "409", description = "Conflito de horário")
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody AgendamentoRequestDto dto) {

        Agendamento novo = new Agendamento();
        novo.setDataHoraInicio(dto.getDataHoraInicio());
        novo.setDataHoraFim(dto.getDataHoraFim());
        novo.setObservacao(dto.getObservacao());

        Agendamento atualizado = service.atualizar(
                id,
                novo,
                dto.getClienteId(),
                dto.getFuncionarioId(),
                dto.getSalaId(),
                dto.getServicoId(),
                dto.getStatusId()
        );

        return ResponseEntity.ok(AgendamentoMapper.toResponse(atualizado));
    }

    @Operation(summary = "Deletar agendamento")
    @ApiResponse(responseCode = "204", description = "Deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}