package sptech.school.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.backend.dto.StatusDto.StatusResponseDto;
import sptech.school.backend.service.StatusService;
import java.util.List;

@RestController
@RequestMapping("/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

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
