package sptech.school.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.AgendamentoRequestDto;
import sptech.school.backend.dto.AgendamentoResponseDto;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.mapper.AgendamentoMapper;
import sptech.school.backend.service.AgendamentoService;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDto> criar(@RequestBody AgendamentoRequestDto dto) {

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(dto.getDataHora());

        Agendamento salvo = service.criar(agendamento, dto.getClienteId());

        return ResponseEntity.status(201).body(AgendamentoMapper.toResponse(salvo));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDto>> listar() {

        List<AgendamentoResponseDto> lista = service.listar()
                .stream()
                .map(AgendamentoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody AgendamentoRequestDto dto) {

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(dto.getDataHora());

        Agendamento atualizado = service.atualizar(id, agendamento, dto.getClienteId());

        return ResponseEntity.ok(AgendamentoMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
