package sptech.school.backend.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.exception.ConflitoException;
import sptech.school.backend.repository.AgendamentoRepository;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@DisplayName("Strategy unitario - SalaDisponivelStrategy")
class SalaDisponivelStrategyTest {

    @Mock
    private AgendamentoRepository repository;

    @InjectMocks
    private SalaDisponivelStrategy strategy;

    @DisplayName("Unidade: SalaDisponivelStrategy | Cenario: validar | Dados: quando sala livre | Verifica: deve passar")
    @Test
    void validar_devePassar_quandoSalaLivre() {
        Agendamento agendamento = agendamento();
        Mockito.when(repository.existeConflitoSala(1L, agendamento.getDataHoraInicio(), agendamento.getDataHoraFim(), 0L))
                .thenReturn(false);

        Assertions.assertDoesNotThrow(() -> strategy.validar(agendamento, 1L, 1L, 0L));
    }

    @DisplayName("Unidade: SalaDisponivelStrategy | Cenario: validar | Dados: quando sala ocupada | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoSalaOcupada() {
        Agendamento agendamento = agendamento();
        Mockito.when(repository.existeConflitoSala(1L, agendamento.getDataHoraInicio(), agendamento.getDataHoraFim(), 0L))
                .thenReturn(true);

        Assertions.assertThrows(
                ConflitoException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    private Agendamento agendamento() {
        Sala sala = new Sala();
        sala.setId(1L);

        Agendamento agendamento = new Agendamento();
        agendamento.setSala(sala);
        agendamento.setDataHoraInicio(LocalDateTime.of(2026, 6, 9, 8, 0));
        agendamento.setDataHoraFim(LocalDateTime.of(2026, 6, 9, 9, 0));
        return agendamento;
    }
}
