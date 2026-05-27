package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.backend.dto.DisponibilidadeDto.DiaDisponivelDto;
import sptech.school.backend.dto.DisponibilidadeDto.HorarioDisponivelDto;
import sptech.school.backend.dto.DisponibilidadeDto.SalaDisponibilidadeDto;
import sptech.school.backend.service.DisponibilidadeService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/disponibilidade")
@Tag(name = "4. Disponibilidade — Consultas")
@SecurityRequirement(name = "bearerAuth")
public class DisponibilidadeController {

    private final DisponibilidadeService disponibilidadeService;

    public DisponibilidadeController(DisponibilidadeService disponibilidadeService) {
        this.disponibilidadeService = disponibilidadeService;
    }

    @GetMapping("/calendario")
    public ResponseEntity<List<DiaDisponivelDto>> calcularCalendario(@RequestParam String mes) {
        return ResponseEntity.ok(disponibilidadeService.calcularCalendario(mes));
    }

    @GetMapping("/horarios")
    public ResponseEntity<List<HorarioDisponivelDto>> calcularHorariosDisponiveis(@RequestParam String data) {
        LocalDate localDate = LocalDate.parse(data);
        return ResponseEntity.ok(disponibilidadeService.calcularHorariosDisponiveis(localDate));
    }

    @GetMapping("/salas")
    public ResponseEntity<List<SalaDisponibilidadeDto>> verificarDisponibilidadeSalas(
            @RequestParam String inicio,
            @RequestParam String fim
    ) {
        LocalDateTime dataHoraInicio = LocalDateTime.parse(inicio);
        LocalDateTime dataHoraFim = LocalDateTime.parse(fim);
        return ResponseEntity.ok(disponibilidadeService.verificarDisponibilidadeSalas(dataHoraInicio, dataHoraFim));
    }
}
