package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.backend.dto.AgendamentoDto.AgendamentoRequestDto;
import sptech.school.backend.dto.AgendamentoDto.AgendamentoResponseDto;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.mapper.AgendamentoMapper;
import sptech.school.backend.service.AgendamentoService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Agendamentos", description = "Criacao, consulta e manutencao de agendamentos")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @Operation(summary = "Criar agendamento", description = "Cria um agendamento vinculando cliente, funcionario, sala, servico e status.")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Regra de negocio invalida")
    @ApiResponse(responseCode = "404", description = "Recurso nao encontrado")
    @ApiResponse(responseCode = "409", description = "Conflito de horario")
    @PostMapping
    public ResponseEntity<AgendamentoResponseDto> criar(@RequestBody AgendamentoRequestDto dto) {
        Agendamento agendamento = toEntity(dto);

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

    @Operation(summary = "Listar agendamentos", description = "Lista agendamentos, com filtros opcionais por periodo e status.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDto>> listar(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime inicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fim,

            @RequestParam(required = false)
            Long statusId
    ) {
        List<Agendamento> agendamentos;

        if ((inicio == null) != (fim == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "inicio e fim devem ser informados juntos");
        }

        if (inicio != null) {
            agendamentos = service.listarPorPeriodo(inicio, fim);
        } else if (statusId != null) {
            agendamentos = service.listarPorStatus(statusId);
        } else {
            agendamentos = service.listar();
        }

        if (statusId != null && inicio != null) {
            agendamentos = agendamentos.stream()
                    .filter(agendamento -> agendamento.getStatus().getId().equals(statusId))
                    .toList();
        }

        return ResponseEntity.ok(toResponseList(agendamentos));
    }

    @Operation(summary = "Buscar agendamento por ID", description = "Retorna um agendamento especifico pelo ID.")
    @ApiResponse(responseCode = "200", description = "Agendamento encontrado")
    @ApiResponse(responseCode = "404", description = "Agendamento nao encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoMapper.toResponse(service.buscarPorId(id)));
    }

    @Operation(summary = "Listar agendamentos do cliente", description = "Retorna agendamentos vinculados ao cliente informado.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/meus/{clienteId}")
    public ResponseEntity<List<AgendamentoResponseDto>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(toResponseList(service.listarPorCliente(clienteId)));
    }

    @Operation(summary = "Atualizar agendamento", description = "Atualiza dados e vinculos de um agendamento existente.")
    @ApiResponse(responseCode = "200", description = "Atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Regra invalida")
    @ApiResponse(responseCode = "404", description = "Nao encontrado")
    @ApiResponse(responseCode = "409", description = "Conflito de horario")
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody AgendamentoRequestDto dto
    ) {
        Agendamento atualizado = service.atualizar(
                id,
                toEntity(dto),
                dto.getClienteId(),
                dto.getFuncionarioId(),
                dto.getSalaId(),
                dto.getServicoId(),
                dto.getStatusId()
        );

        return ResponseEntity.ok(AgendamentoMapper.toResponse(atualizado));
    }

    @Operation(summary = "Excluir agendamento", description = "Remove um agendamento existente pelo ID.")
    @ApiResponse(responseCode = "204", description = "Deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Nao encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private Agendamento toEntity(AgendamentoRequestDto dto) {
        Agendamento agendamento = new Agendamento();
        agendamento.setDataHoraInicio(dto.getDataHoraInicio());
        agendamento.setDataHoraFim(dto.getDataHoraFim());
        agendamento.setObservacao(dto.getObservacao());
        return agendamento;
    }

    private List<AgendamentoResponseDto> toResponseList(List<Agendamento> agendamentos) {
        return agendamentos.stream()
                .map(AgendamentoMapper::toResponse)
                .toList();
    }
}
