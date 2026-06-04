package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import sptech.school.backend.dto.AgendaDto.AgendaExcecaoDto;
import sptech.school.backend.dto.AgendaDto.AgendaFuncionarioDto;
import sptech.school.backend.dto.AgendaDto.AgendaFuncionarioListagemDto;
import sptech.school.backend.entity.AgendaExcecao;
import sptech.school.backend.entity.AgendaFuncionario;
import sptech.school.backend.service.AgendaFuncionarioService;
import java.util.List;

@RestController
@RequestMapping("/agenda-funcionario")
public class AgendaFuncionarioController {

    private final AgendaFuncionarioService service;

    public AgendaFuncionarioController(AgendaFuncionarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os profissionais com suas agendas semanais")
    @GetMapping
    public ResponseEntity<List<AgendaFuncionarioListagemDto>> listarTodosComAgenda() {
        return ResponseEntity.ok(service.listarTodosComAgenda());
    }

    @GetMapping("/{funcionarioId}")
    public ResponseEntity<List<AgendaFuncionario>> listarPorFuncionario(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.listarPorFuncionario(funcionarioId));
    }

    @PostMapping
    public ResponseEntity<AgendaFuncionario> criar(@Valid @RequestBody AgendaFuncionarioDto dto) {
        return ResponseEntity.status(201).body(service.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendaFuncionario> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendaFuncionarioDto dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{funcionarioId}/excecoes")
    public ResponseEntity<List<AgendaExcecao>> listarExcecoesPorFuncionario(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.listarExcecoesPorFuncionario(funcionarioId));
    }

    @PostMapping("/excecoes")
    public ResponseEntity<AgendaExcecao> criarExcecao(@RequestBody AgendaExcecaoDto dto) {
        return ResponseEntity.status(201).body(service.criarExcecao(dto));
    }

    @DeleteMapping("/excecoes/{id}")
    public ResponseEntity<Void> deletarExcecao(@PathVariable Long id) {
        service.deletarExcecao(id);
        return ResponseEntity.noContent().build();
    }
}
