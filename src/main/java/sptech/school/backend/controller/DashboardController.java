package sptech.school.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.DashboardDto.AgendamentoDiasSemanaDto;
import sptech.school.backend.dto.DashboardDto.ServicoDadosDto;
import sptech.school.backend.service.DashboardService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard e Indicadores", description = "Indicadores gerenciais de agendamentos, clientes e servicos")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "Total de agendamentos ativos", description = "Conta agendamentos ativos dentro do periodo informado.")
    @GetMapping("/total-agendamentos")
    public ResponseEntity<Integer> totalAgendamentos(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(
                dashboardService.totalAgendamentosAtivos(inicio, fim)
        );
    }

    @Operation(summary = "Servicos mais usados", description = "Lista a quantidade de servicos realizados no periodo.")
    @GetMapping("/servicos")
    public ResponseEntity<List<ServicoDadosDto>> servicosMaisUsados(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(
                dashboardService.quantidadeServicosUltimosDias(inicio, fim)
        );
    }


    @Operation(summary = "Agendamentos por dia da semana", description = "Retorna a distribuicao de agendamentos por dia da semana.")
    @GetMapping("/agendamentos-dia-semana")
    public ResponseEntity<List<AgendamentoDiasSemanaDto>> porDiaSemana(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(
                dashboardService.buscarPorDiaSemana(inicio, fim)
        );
    }

    @Operation(summary = "Total de cancelamentos", description = "Conta cancelamentos dentro do periodo informado.")
    @GetMapping("/cancelamentos")
    public ResponseEntity<Integer> cancelamentos(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(
                dashboardService.totalCancelamentos(inicio, fim)
        );
    }

    @Operation(summary = "Clientes ativos", description = "Conta clientes com atividade dentro do periodo informado.")
    @GetMapping("/clientes-ativos")
    public ResponseEntity<Integer> clientesAtivos(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(
                dashboardService.clientesAtivos(inicio, fim)
        );
    }

    @Operation(summary = "Clientes novos", description = "Conta clientes novos a partir da data informada.")
    @GetMapping("/clientes-novos")
    public ResponseEntity<Integer> clientesNovos(
            @RequestParam LocalDate inicio
    ) {
        return ResponseEntity.ok(
                dashboardService.clientesNovos(inicio)
        );
    }
}