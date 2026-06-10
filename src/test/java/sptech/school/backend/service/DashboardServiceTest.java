package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.dto.DashboardDto.AgendamentoDiasSemanaDto;
import sptech.school.backend.dto.DashboardDto.ServicoDadosDto;
import sptech.school.backend.repository.AgendamentoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - DashboardService")
class DashboardServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @InjectMocks
    private DashboardService service;

    @DisplayName("Unidade: DashboardService | Cenario: total agendamentos ativos | Dados: dados preparados no arrange do teste | Verifica: deve retornar valor")
    @Test
    void totalAgendamentosAtivos_deveRetornarValor() {
        Mockito.when(agendamentoRepository.findByTotalAgendamentos(
                LocalDateTime.of(2026, 6, 1, 0, 0),
                LocalDateTime.of(2026, 6, 30, 0, 0)
        )).thenReturn(10);

        Integer resultado = service.totalAgendamentosAtivos(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

        Assertions.assertEquals(10, resultado);
    }

    @DisplayName("Unidade: DashboardService | Cenario: quantidade servicos ultimos dias | Dados: dados preparados no arrange do teste | Verifica: deve retornar lista")
    @Test
    void quantidadeServicosUltimosDias_deveRetornarLista() {
        List<ServicoDadosDto> dados = List.of(new ServicoDadosDto("Acupuntura", 3L));
        Mockito.when(agendamentoRepository.findByQuantidadeDeCadaServico(
                LocalDateTime.of(2026, 6, 1, 0, 0),
                LocalDateTime.of(2026, 6, 30, 23, 59, 59)
        )).thenReturn(dados);

        List<ServicoDadosDto> resultado = service.quantidadeServicosUltimosDias(
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30)
        );

        Assertions.assertEquals(dados, resultado);
    }

    @DisplayName("Unidade: DashboardService | Cenario: buscar por dia semana | Dados: dados preparados no arrange do teste | Verifica: deve retornar 7 dias")
    @Test
    void buscarPorDiaSemana_deveRetornar7Dias() {
        Mockito.when(agendamentoRepository.findByTotalAtendimentoDiaSemana(
                LocalDateTime.of(2026, 6, 1, 0, 0),
                LocalDateTime.of(2026, 6, 30, 23, 59, 59)
        )).thenReturn(List.of(new AgendamentoDiasSemanaDto("Monday", 2L)));

        List<AgendamentoDiasSemanaDto> resultado = service.buscarPorDiaSemana(
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30)
        );

        Assertions.assertEquals(7, resultado.size());
        Assertions.assertEquals(2L, resultado.get(1).getTotal());
    }

    @DisplayName("Unidade: DashboardService | Cenario: buscar por dia semana | Dados: quando repositorio retorna dia duplicado | Verifica: deve manter primeiro valor")
    @Test
    void buscarPorDiaSemana_deveManterPrimeiroValor_quandoRepositorioRetornaDiaDuplicado() {
        Mockito.when(agendamentoRepository.findByTotalAtendimentoDiaSemana(
                LocalDateTime.of(2026, 6, 1, 0, 0),
                LocalDateTime.of(2026, 6, 30, 23, 59, 59)
        )).thenReturn(List.of(
                new AgendamentoDiasSemanaDto("Monday", 2L),
                new AgendamentoDiasSemanaDto("Monday", 9L)
        ));

        List<AgendamentoDiasSemanaDto> resultado = service.buscarPorDiaSemana(
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30)
        );

        Assertions.assertEquals(2L, resultado.get(1).getTotal());
    }

    @DisplayName("Unidade: DashboardService | Cenario: total cancelamentos | Dados: dados preparados no arrange do teste | Verifica: deve retornar valor")
    @Test
    void totalCancelamentos_deveRetornarValor() {
        Mockito.when(agendamentoRepository.findByTotalCancelamentos(
                LocalDateTime.of(2026, 6, 1, 0, 0),
                LocalDateTime.of(2026, 6, 30, 23, 59, 59)
        )).thenReturn(4);

        Integer resultado = service.totalCancelamentos(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

        Assertions.assertEquals(4, resultado);
    }

    @DisplayName("Unidade: DashboardService | Cenario: clientes ativos | Dados: dados preparados no arrange do teste | Verifica: deve retornar valor")
    @Test
    void clientesAtivos_deveRetornarValor() {
        Mockito.when(agendamentoRepository.findByClienteAtivosNoPeriodo(
                LocalDateTime.of(2026, 6, 1, 0, 0),
                LocalDateTime.of(2026, 6, 30, 23, 59, 59)
        )).thenReturn(5);

        Integer resultado = service.clientesAtivos(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

        Assertions.assertEquals(5, resultado);
    }

    @DisplayName("Unidade: DashboardService | Cenario: clientes novos | Dados: dados preparados no arrange do teste | Verifica: deve retornar valor")
    @Test
    void clientesNovos_deveRetornarValor() {
        Mockito.when(agendamentoRepository.findByClientesNovosPeriodo(LocalDateTime.of(2026, 6, 1, 0, 0)))
                .thenReturn(2);

        Integer resultado = service.clientesNovos(LocalDate.of(2026, 6, 1));

        Assertions.assertEquals(2, resultado);
    }
}
