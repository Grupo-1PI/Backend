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
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.exception.RegraNegocioException;
import sptech.school.backend.repository.ServicoRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Strategy unitario - ValidacaoSalaServicoStrategy")
class ValidacaoSalaServicoStrategyTest {

    @Mock
    private ServicoRepository servicoRepository;

    @InjectMocks
    private ValidacaoSalaServicoStrategy strategy;

    @DisplayName("Unidade: ValidacaoSalaServicoStrategy | Cenario: validar | Dados: quando sala atende servico | Verifica: deve passar")
    @Test
    void validar_devePassar_quandoSalaAtendeServico() {
        Agendamento agendamento = agendamento(sala(1L));
        Servico servico = servico();
        servico.getSalas().add(sala(1L));
        Mockito.when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));

        Assertions.assertDoesNotThrow(() -> strategy.validar(agendamento, 1L, 1L, 0L));
    }

    @DisplayName("Unidade: ValidacaoSalaServicoStrategy | Cenario: validar | Dados: quando sala nao atende servico | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoSalaNaoAtendeServico() {
        Agendamento agendamento = agendamento(sala(1L));
        Servico servico = servico();
        servico.getSalas().add(sala(2L));
        Mockito.when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));

        Assertions.assertThrows(
                RegraNegocioException.class,
                () -> strategy.validar(agendamento, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: ValidacaoSalaServicoStrategy | Cenario: validar | Dados: quando servico nao encontrado | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoServicoNaoEncontrado() {
        Mockito.when(servicoRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> strategy.validar(agendamento(sala(1L)), 1L, 1L, 0L)
        );
    }

    private Agendamento agendamento(Sala sala) {
        Agendamento agendamento = new Agendamento();
        agendamento.setSala(sala);
        return agendamento;
    }

    private Sala sala(Long id) {
        Sala sala = new Sala();
        sala.setId(id);
        return sala;
    }

    private Servico servico() {
        Servico servico = new Servico();
        servico.setId(1L);
        return servico;
    }
}
