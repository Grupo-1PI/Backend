package sptech.school.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Disponibilidade - Consultas", description = "Consulta calendario, horarios e salas disponiveis")
@SecurityRequirement(name = "bearerAuth")
public class DisponibilidadeController {

    private final DisponibilidadeService disponibilidadeService;

    public DisponibilidadeController(DisponibilidadeService disponibilidadeService) {
        this.disponibilidadeService = disponibilidadeService;
    }

    @Operation(summary = "Consultar calendario mensal", description = "Retorna o status de disponibilidade de cada dia do mes informado.")
    @GetMapping("/calendario")
    public ResponseEntity<List<DiaDisponivelDto>> calcularCalendario(@RequestParam String mes) {
        return ResponseEntity.ok(disponibilidadeService.calcularCalendario(mes));
    }

    @Operation(summary = "Consultar horarios disponiveis", description = "Retorna slots disponiveis e ocupados para a data informada.")
    @GetMapping("/horarios")
    public ResponseEntity<List<HorarioDisponivelDto>> calcularHorariosDisponiveis(@RequestParam String data) {
        LocalDate localDate = LocalDate.parse(data);
        return ResponseEntity.ok(disponibilidadeService.calcularHorariosDisponiveis(localDate));
    }

    @Operation(summary = "Consultar disponibilidade das salas", description = "Retorna se cada sala esta ocupada no intervalo informado.")
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
