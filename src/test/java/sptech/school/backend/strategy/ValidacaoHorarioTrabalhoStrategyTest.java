package sptech.school.backend.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.entity.AgendaFuncionario;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.exception.RegraNegocioException;
import sptech.school.backend.repository.AgendaFuncionarioRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Strategy unitario - ValidacaoHorarioTrabalhoStrategy")
class ValidacaoHorarioTrabalhoStrategyTest {

    @Mock
    private AgendaFuncionarioRepository agendaFuncionarioRepository;

    @InjectMocks
    private ValidacaoHorarioTrabalhoStrategy strategy;

    @DisplayName("Unidade: ValidacaoHorarioTrabalhoStrategy | Cenario: validar | Dados: quando dentro do horario | Verifica: deve passar")
    @Test
    void validar_devePassar_quandoDentroDoHorario() {
        Agendamento agendamento = agendamento(
                LocalDateTime.of(2026, 6, 8, 8, 0),
                LocalDateTime.of(2026, 6, 8, 9, 0)
        );
        Mockito.when(agendaFuncionarioRepository.findByFuncionarioId(1L))
                .thenReturn(List.of(agenda(2, LocalTime.of(8, 0), LocalTime.of(12, 0))));

        Assertions.assertDoesNotThrow(() -> strategy.validar(agendamento, 1L, 1L, 0L));
    }

    @DisplayName("Unidade: ValidacaoHorarioTrabalhoStrategy | Cenario: validar | Dados: quando domingo converte para dia semana mysql um | Verifica: deve passar")
    @Test
    void validar_devePassar_quandoDomingoConverteParaDiaSemanaMysqlUm() {
        Agendamento agendamento = agendamento(
                LocalDateTime.of(2026, 6, 7, 8, 0),
                LocalDateTime.of(2026, 6, 7, 9, 0)
        );
        Mockito.when(agendaFuncionarioRepository.findByFuncionarioId(1L))
                .thenReturn(List.of(agenda(1, LocalTime.of(8, 0), LocalTime.of(12, 0))));

        Assertions.assertDoesNotThrow(() -> strategy.validar(agendamento, 1L, 1L, 0L));
    }

    @DisplayName("Unidade: ValidacaoHorarioTrabalhoStrategy | Cenario: validar | Dados: quando fora do horario | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoForaDoHorario() {
        Agendamento agendamento = agendamento(
                LocalDateTime.of(2026, 6, 8, 20, 0),
                LocalDateTime.of(2026, 6, 8, 21, 0)
        );
        Mockito.when(agendaFuncionarioRepository.findByFuncionarioId(1L))
                .thenReturn(List.of(agenda(2, LocalTime.of(8, 0), LocalTime.of(12, 0))));

        Assertions.assertThrows(
                RegraNegocioException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: ValidacaoHorarioTrabalhoStrategy | Cenario: validar | Dados: quando sem agenda no dia | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoSemAgendaNoDia() {
        Agendamento agendamento = agendamento(
                LocalDateTime.of(2026, 6, 8, 8, 0),
                LocalDateTime.of(2026, 6, 8, 9, 0)
        );
        Mockito.when(agendaFuncionarioRepository.findByFuncionarioId(1L)).thenReturn(List.of());

        Assertions.assertThrows(
                RegraNegocioException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: ValidacaoHorarioTrabalhoStrategy | Cenario: validar | Dados: quando agenda e dia diferente | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoAgendaEDiaDiferente() {
        Agendamento agendamento = agendamento(
                LocalDateTime.of(2026, 6, 8, 8, 0),
                LocalDateTime.of(2026, 6, 8, 9, 0)
        );
        Mockito.when(agendaFuncionarioRepository.findByFuncionarioId(1L))
                .thenReturn(List.of(agenda(3, LocalTime.of(8, 0), LocalTime.of(12, 0))));

        Assertions.assertThrows(
                RegraNegocioException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: ValidacaoHorarioTrabalhoStrategy | Cenario: validar | Dados: quando inicio antes da agenda | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoInicioAntesDaAgenda() {
        Agendamento agendamento = agendamento(
                LocalDateTime.of(2026, 6, 8, 7, 0),
                LocalDateTime.of(2026, 6, 8, 9, 0)
        );
        Mockito.when(agendaFuncionarioRepository.findByFuncionarioId(1L))
                .thenReturn(List.of(agenda(2, LocalTime.of(8, 0), LocalTime.of(12, 0))));

        Assertions.assertThrows(
                RegraNegocioException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: ValidacaoHorarioTrabalhoStrategy | Cenario: validar | Dados: quando fim depois da agenda | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoFimDepoisDaAgenda() {
        Agendamento agendamento = agendamento(
                LocalDateTime.of(2026, 6, 8, 11, 0),
                LocalDateTime.of(2026, 6, 8, 13, 0)
        );
        Mockito.when(agendaFuncionarioRepository.findByFuncionarioId(1L))
                .thenReturn(List.of(agenda(2, LocalTime.of(8, 0), LocalTime.of(12, 0))));

        Assertions.assertThrows(
                RegraNegocioException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    private Agendamento agendamento(LocalDateTime inicio, LocalDateTime fim) {
        Agendamento agendamento = new Agendamento();
        agendamento.setDataHoraInicio(inicio);
        agendamento.setDataHoraFim(fim);
        return agendamento;
    }

    private AgendaFuncionario agenda(Integer diaSemana, LocalTime inicio, LocalTime fim) {
        AgendaFuncionario agenda = new AgendaFuncionario();
        agenda.setDiaSemana(diaSemana);
        agenda.setHoraInicio(inicio);
        agenda.setHoraFim(fim);
        return agenda;
    }
}
