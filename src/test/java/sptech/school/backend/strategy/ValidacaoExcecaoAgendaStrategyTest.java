package sptech.school.backend.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.entity.AgendaExcecao;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.exception.ConflitoException;
import sptech.school.backend.repository.AgendaExcecaoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Strategy unitario - ValidacaoExcecaoAgendaStrategy")
class ValidacaoExcecaoAgendaStrategyTest {

    @Mock
    private AgendaExcecaoRepository agendaExcecaoRepository;

    @InjectMocks
    private ValidacaoExcecaoAgendaStrategy strategy;

    @DisplayName("Unidade: ValidacaoExcecaoAgendaStrategy | Cenario: validar | Dados: quando sem excecao | Verifica: deve passar")
    @Test
    void validar_devePassar_quandoSemExcecao() {
        Agendamento agendamento = agendamento();
        Mockito.when(agendaExcecaoRepository.findByFuncionarioIdAndData(1L, LocalDate.of(2026, 6, 9)))
                .thenReturn(List.of());

        Assertions.assertDoesNotThrow(() -> strategy.validar(agendamento, 1L, 1L, 0L));
    }

    @DisplayName("Unidade: ValidacaoExcecaoAgendaStrategy | Cenario: validar | Dados: quando dia inteiro bloqueado | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoDiaInteiroBloqueado() {
        Agendamento agendamento = agendamento();
        Mockito.when(agendaExcecaoRepository.findByFuncionarioIdAndData(1L, LocalDate.of(2026, 6, 9)))
                .thenReturn(List.of(excecao(false, null, null)));

        Assertions.assertThrows(
                ConflitoException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: ValidacaoExcecaoAgendaStrategy | Cenario: validar | Dados: quando excecao tem apenas hora fim nula | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoExcecaoTemApenasHoraFimNula() {
        Agendamento agendamento = agendamento();
        Mockito.when(agendaExcecaoRepository.findByFuncionarioIdAndData(1L, LocalDate.of(2026, 6, 9)))
                .thenReturn(List.of(excecao(false, LocalTime.of(8, 0), null)));

        Assertions.assertThrows(
                ConflitoException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: ValidacaoExcecaoAgendaStrategy | Cenario: validar | Dados: quando horario bloqueado | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoHorarioBloqueado() {
        Agendamento agendamento = agendamento();
        Mockito.when(agendaExcecaoRepository.findByFuncionarioIdAndData(1L, LocalDate.of(2026, 6, 9)))
                .thenReturn(List.of(excecao(false, LocalTime.of(8, 0), LocalTime.of(12, 0))));

        Assertions.assertThrows(
                ConflitoException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: ValidacaoExcecaoAgendaStrategy | Cenario: validar | Dados: quando excecao disponivel | Verifica: deve passar")
    @Test
    void validar_devePassar_quandoExcecaoDisponivel() {
        Agendamento agendamento = agendamento();
        Mockito.when(agendaExcecaoRepository.findByFuncionarioIdAndData(1L, LocalDate.of(2026, 6, 9)))
                .thenReturn(List.of(excecao(true, LocalTime.of(8, 0), LocalTime.of(12, 0))));

        Assertions.assertDoesNotThrow(() -> strategy.validar(agendamento, 1L, 1L, 0L));
    }

    @DisplayName("Unidade: ValidacaoExcecaoAgendaStrategy | Cenario: validar | Dados: quando horario bloqueado nao sobrepoe agendamento | Verifica: deve passar")
    @Test
    void validar_devePassar_quandoHorarioBloqueadoNaoSobrepoeAgendamento() {
        Agendamento agendamento = agendamento();
        Mockito.when(agendaExcecaoRepository.findByFuncionarioIdAndData(1L, LocalDate.of(2026, 6, 9)))
                .thenReturn(List.of(excecao(false, LocalTime.of(10, 0), LocalTime.of(12, 0))));

        Assertions.assertDoesNotThrow(() -> strategy.validar(agendamento, 1L, 1L, 0L));
    }

    @DisplayName("Unidade: ValidacaoExcecaoAgendaStrategy | Cenario: validar | Dados: quando agendamento comeca no fim da excecao | Verifica: deve passar")
    @Test
    void validar_devePassar_quandoAgendamentoComecaNoFimDaExcecao() {
        Agendamento agendamento = agendamento();
        Mockito.when(agendaExcecaoRepository.findByFuncionarioIdAndData(1L, LocalDate.of(2026, 6, 9)))
                .thenReturn(List.of(excecao(false, LocalTime.of(7, 0), LocalTime.of(9, 0))));

        Assertions.assertDoesNotThrow(() -> strategy.validar(agendamento, 1L, 1L, 0L));
    }

    private Agendamento agendamento() {
        Agendamento agendamento = new Agendamento();
        agendamento.setDataHoraInicio(LocalDateTime.of(2026, 6, 9, 9, 0));
        agendamento.setDataHoraFim(LocalDateTime.of(2026, 6, 9, 10, 0));
        return agendamento;
    }

    private AgendaExcecao excecao(Boolean disponivel, LocalTime inicio, LocalTime fim) {
        AgendaExcecao excecao = new AgendaExcecao();
        excecao.setDisponivel(disponivel);
        excecao.setHoraInicio(inicio);
        excecao.setHoraFim(fim);
        return excecao;
    }
}
