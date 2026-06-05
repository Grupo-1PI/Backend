package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.backend.dto.ServicoDto.ServicoResumoDto;
import sptech.school.backend.mapper.ServicoMapper;
import sptech.school.backend.repository.ServicoRepository;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "5d. Gerenciamento - Servicos")
@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoRepository repository;

    public ServicoController(ServicoRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Listar servicos")
    @ApiResponse(responseCode = "200", description = "Lista de servicos retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ServicoResumoDto>> listar() {
        List<ServicoResumoDto> lista = repository.findAll()
                .stream()
                .map(ServicoMapper::toResumo)
                .toList();

        return ResponseEntity.ok(lista);
    }
}
