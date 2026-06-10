package sptech.school.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sptech.school.backend.exception.GlobalExceptionHandler;
import sptech.school.backend.dto.DashboardDto.AgendamentoDiasSemanaDto;
import sptech.school.backend.dto.DashboardDto.ServicoDadosDto;
import sptech.school.backend.service.DashboardService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - DashboardController")
class DashboardControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private DashboardService dashboardService;

    @DisplayName("Unidade: DashboardController | Cenario: get total agendamentos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getTotalAgendamentos_deveRetornar200() throws Exception {
        Mockito.when(dashboardService.totalAgendamentosAtivos(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)))
                .thenReturn(10);

        mockMvc.perform(get("/dashboard/total-agendamentos")
                        .param("inicio", "2026-06-01")
                        .param("fim", "2026-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10));
    }

    @DisplayName("Unidade: DashboardController | Cenario: get servicos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getServicos_deveRetornar200() throws Exception {
        Mockito.when(dashboardService.quantidadeServicosUltimosDias(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)))
                .thenReturn(List.of(new ServicoDadosDto("Acupuntura", 2L)));

        mockMvc.perform(get("/dashboard/servicos")
                        .param("inicio", "2026-06-01")
                        .param("fim", "2026-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Acupuntura"));
    }

    @DisplayName("Unidade: DashboardController | Cenario: get agendamentos dia semana | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getAgendamentosDiaSemana_deveRetornar200() throws Exception {
        Mockito.when(dashboardService.buscarPorDiaSemana(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)))
                .thenReturn(List.of(new AgendamentoDiasSemanaDto("Monday", 2L)));

        mockMvc.perform(get("/dashboard/agendamentos-dia-semana")
                        .param("inicio", "2026-06-01")
                        .param("fim", "2026-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].diaSemana").value("Monday"));
    }

    @DisplayName("Unidade: DashboardController | Cenario: get cancelamentos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getCancelamentos_deveRetornar200() throws Exception {
        Mockito.when(dashboardService.totalCancelamentos(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)))
                .thenReturn(1);

        mockMvc.perform(get("/dashboard/cancelamentos")
                        .param("inicio", "2026-06-01")
                        .param("fim", "2026-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @DisplayName("Unidade: DashboardController | Cenario: get clientes ativos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getClientesAtivos_deveRetornar200() throws Exception {
        Mockito.when(dashboardService.clientesAtivos(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)))
                .thenReturn(5);

        mockMvc.perform(get("/dashboard/clientes-ativos")
                        .param("inicio", "2026-06-01")
                        .param("fim", "2026-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));
    }

    @DisplayName("Unidade: DashboardController | Cenario: get clientes novos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getClientesNovos_deveRetornar200() throws Exception {
        Mockito.when(dashboardService.clientesNovos(LocalDate.of(2026, 6, 1))).thenReturn(3);

        mockMvc.perform(get("/dashboard/clientes-novos").param("inicio", "2026-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3));
    }
}
