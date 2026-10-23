package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.TriagemDto.TriagemPerguntaCriacaoDto;
import sptech.school.backend.dto.TriagemDto.TriagemPerguntaResponseDto;
import sptech.school.backend.dto.TriagemDto.TriagemResultadoDto;
import sptech.school.backend.dto.TriagemDto.RespostaTriagemRequestDto;
import sptech.school.backend.entity.RespostaEnum;
import sptech.school.backend.service.TriagemService;
import java.util.List;

@RestController
@RequestMapping("/triagem")
@Tag(name = "Triagem Dinâmica", description = "Questionário dinâmico para recomendação de especialidade")
@SecurityRequirement(name = "bearerAuth")
public class TriagemController {

    private final TriagemService service;

    public TriagemController(TriagemService service) {
        this.service = service;
    }

    // ===== PÚBLICO (Paciente) =====

    @Operation(summary = "Iniciar triagem", description = "Retorna a primeira pergunta do questionário")
    @ApiResponse(responseCode = "200", description = "Pergunta inicial retornada")
    @ApiResponse(responseCode = "500", description = "Nenhuma pergunta raiz configurada")
    @GetMapping("/iniciar")
    public ResponseEntity<TriagemPerguntaResponseDto> iniciar() {
        return ResponseEntity.ok(service.iniciar());
    }

    @Operation(summary = "Responder pergunta da triagem", description = "Processa a resposta e retorna próxima pergunta ou especialidade final")
    @ApiResponse(responseCode = "200", description = "Próxima pergunta ou especialidade recomendada")
    @ApiResponse(responseCode = "404", description = "Pergunta ou resposta não encontrada")
    @PostMapping("/responder")
    public ResponseEntity<TriagemResultadoDto> responder(@Valid @RequestBody RespostaTriagemRequestDto request) {
        return ResponseEntity.ok(service.responder(request.getPerguntaId(), request.getResposta()));
    }

    // ===== ADMIN (CRUD) =====

    @Operation(summary = "Listar todas as perguntas da triagem")
    @GetMapping
    @PreAuthorize("hasAuthority('CRUD_AGENDAMENTO')")
    public ResponseEntity<List<TriagemPerguntaResponseDto>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Buscar pergunta por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CRUD_AGENDAMENTO')")
    public ResponseEntity<TriagemPerguntaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Criar pergunta com respostas")
    @PostMapping
    @PreAuthorize("hasAuthority('CRUD_AGENDAMENTO')")
    public ResponseEntity<TriagemPerguntaResponseDto> criar(@Valid @RequestBody TriagemPerguntaCriacaoDto dto) {
        return ResponseEntity.status(201).body(service.criar(dto));
    }

    @Operation(summary = "Atualizar pergunta e respostas")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CRUD_AGENDAMENTO')")
    public ResponseEntity<TriagemPerguntaResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody TriagemPerguntaCriacaoDto dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Excluir pergunta")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CRUD_AGENDAMENTO')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}