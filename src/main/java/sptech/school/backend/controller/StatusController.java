package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.backend.dto.StatusDto.StatusResponseDto;
import sptech.school.backend.service.StatusService;
import java.util.List;

@RestController
@RequestMapping("/status")
@Tag(name = "Administracao - Status", description = "Consulta dos status de agendamento")
@SecurityRequirement(name = "bearerAuth")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @Operation(summary = "Listar status", description = "Retorna todos os status disponiveis para agendamentos.")
    @GetMapping
    public ResponseEntity<List<StatusResponseDto>> listar() {
        return ResponseEntity.ok(
                statusService.listar()
                        .stream()
                        .map(status -> new StatusResponseDto(status.getId(), status.getNome()))
                        .toList()
        );
    }
}
