package sptech.school.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.DashboardDto.AgendamentoDiasSemanaDto;
import sptech.school.backend.dto.DashboardDto.ServicoDadosDto;
import sptech.school.backend.service.DashboardService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/total-agendamentos")
    public ResponseEntity<Integer> totalAgendamentos(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(
                dashboardService.totalAgendamentosAtivos(inicio, fim)
        );
    }

    @GetMapping("/servicos")
    public ResponseEntity<List<ServicoDadosDto>> servicosMaisUsados(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(
                dashboardService.quantidadeServicosUltimosDias(inicio, fim)
        );
    }

    @GetMapping("/agendamentos-dia-semana")
    public ResponseEntity<List<AgendamentoDiasSemanaDto>> porDiaSemana(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(
                dashboardService.buscarPorDiaSemana(inicio, fim)
        );
    }

    @GetMapping("/cancelamentos")
    public ResponseEntity<Integer> cancelamentos(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(
                dashboardService.totalCancelamentos(inicio, fim)
        );
    }

    @GetMapping("/clientes-ativos")
    public ResponseEntity<Integer> clientesAtivos(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(
                dashboardService.clientesAtivos(inicio, fim)
        );
    }

    @GetMapping("/clientes-novos")
    public ResponseEntity<Integer> clientesNovos(
            @RequestParam LocalDate inicio
    ) {
        return ResponseEntity.ok(
                dashboardService.clientesNovos(inicio)
        );
    }
}