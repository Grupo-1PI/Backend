package sptech.school.backend.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.exception.RegraNegocioException;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@DisplayName("Strategy unitario - HorarioValidoStrategy")
class HorarioValidoStrategyTest {

    @InjectMocks
    private HorarioValidoStrategy strategy;

    @DisplayName("Unidade: HorarioValidoStrategy | Cenario: validar | Dados: quando horario valido | Verifica: deve passar")
    @Test
    void validar_devePassar_quandoHorarioValido() {
        Agendamento agendamento = agendamento(
                LocalDateTime.of(2026, 6, 9, 8, 0),
                LocalDateTime.of(2026, 6, 9, 9, 0)
        );

        Assertions.assertDoesNotThrow(() -> strategy.validar(agendamento, 1L, 1L, 0L));
    }

    @DisplayName("Unidade: HorarioValidoStrategy | Cenario: validar | Dados: quando data hora inicio nula | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoDataHoraInicioNula() {
        Agendamento agendamento = agendamento(null, LocalDateTime.of(2026, 6, 9, 9, 0));

        Assertions.assertThrows(
                RegraNegocioException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: HorarioValidoStrategy | Cenario: validar | Dados: quando data hora fim nula | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoDataHoraFimNula() {
        Agendamento agendamento = agendamento(LocalDateTime.of(2026, 6, 9, 8, 0), null);

        Assertions.assertThrows(
                RegraNegocioException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: HorarioValidoStrategy | Cenario: validar | Dados: quando fim igual inicio | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoFimIgualInicio() {
        LocalDateTime horario = LocalDateTime.of(2026, 6, 9, 8, 0);
        Agendamento agendamento = agendamento(horario, horario);

        Assertions.assertThrows(
                RegraNegocioException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: HorarioValidoStrategy | Cenario: validar | Dados: quando fim antes do inicio | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoFimAntesDoInicio() {
        Agendamento agendamento = agendamento(
                LocalDateTime.of(2026, 6, 9, 9, 0),
                LocalDateTime.of(2026, 6, 9, 8, 0)
        );

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
}
