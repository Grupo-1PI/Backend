package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Disponibilidade - Agenda dos Funcionarios", description = "Cadastro de agendas semanais e excecoes de disponibilidade")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyAuthority('CRUD_AGENDAMENTO', 'REALIZAR_ATENDIMENTO')")
public class AgendaFuncionarioController {

    private final AgendaFuncionarioService service;

    public AgendaFuncionarioController(AgendaFuncionarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar agendas por profissional", description = "Retorna todos os funcionarios com suas agendas semanais agrupadas.")
    @GetMapping
    public ResponseEntity<List<AgendaFuncionarioListagemDto>> listarTodosComAgenda() {
        return ResponseEntity.ok(service.listarTodosComAgenda());
    }

    @Operation(summary = "Listar agenda de um funcionario", description = "Retorna as faixas semanais cadastradas para o funcionario informado.")
    @GetMapping("/{funcionarioId}")
    public ResponseEntity<List<AgendaFuncionario>> listarPorFuncionario(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.listarPorFuncionario(funcionarioId));
    }


    @Operation(summary = "Criar agenda semanal", description = "Cria uma faixa de trabalho semanal para um funcionario.")
    @PostMapping
    public ResponseEntity<AgendaFuncionario> criar(@Valid @RequestBody AgendaFuncionarioDto dto) {
        return ResponseEntity.status(201).body(service.criar(dto));
    }

    @Operation(summary = "Atualizar agenda semanal", description = "Atualiza uma faixa de trabalho semanal existente.")
    @PutMapping("/{id}")
    public ResponseEntity<AgendaFuncionario> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendaFuncionarioDto dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Excluir agenda semanal", description = "Remove uma faixa de trabalho semanal existente.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar excecoes de agenda", description = "Retorna bloqueios ou liberacoes excepcionais de um funcionario.")
    @GetMapping("/{funcionarioId}/excecoes")
    public ResponseEntity<List<AgendaExcecao>> listarExcecoesPorFuncionario(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.listarExcecoesPorFuncionario(funcionarioId));
    }

    @Operation(summary = "Criar excecao de agenda", description = "Cria um bloqueio ou liberacao excepcional na agenda de um funcionario.")
    @PostMapping("/excecoes")
    public ResponseEntity<AgendaExcecao> criarExcecao(@RequestBody AgendaExcecaoDto dto) {
        return ResponseEntity.status(201).body(service.criarExcecao(dto));
    }

    @Operation(summary = "Excluir excecao de agenda", description = "Remove uma excecao de agenda existente.")
    @DeleteMapping("/excecoes/{id}")
    public ResponseEntity<Void> deletarExcecao(@PathVariable Long id) {
        service.deletarExcecao(id);
        return ResponseEntity.noContent().build();
    }
}
