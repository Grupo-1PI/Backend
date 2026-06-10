package sptech.school.backend.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.entity.Especialidade;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.exception.RegraNegocioException;
import sptech.school.backend.repository.FuncionarioRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Strategy unitario - ValidacaoFuncionarioEspecialidadeStrategy")
class ValidacaoFuncionarioEspecialidadeStrategyTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private ValidacaoFuncionarioEspecialidadeStrategy strategy;

    @DisplayName("Unidade: ValidacaoFuncionarioEspecialidadeStrategy | Cenario: validar | Dados: quando funcionario possui especialidade | Verifica: deve passar")
    @Test
    void validar_devePassar_quandoFuncionarioPossuiEspecialidade() {
        Funcionario funcionario = funcionarioComServicos(List.of(servico(1L)));
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));

        Assertions.assertDoesNotThrow(() -> strategy.validar(null, 1L, 1L, 0L));
    }

    @DisplayName("Unidade: ValidacaoFuncionarioEspecialidadeStrategy | Cenario: validar | Dados: quando funcionario sem especialidade | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoFuncionarioSemEspecialidade() {
        Funcionario funcionario = new Funcionario();
        funcionario.setEspecialidades(List.of());
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));

        Assertions.assertThrows(
                RegraNegocioException.class,
                () -> strategy.validar(null, 1L, 1L, 0L)
        );
    }

    @DisplayName("Unidade: ValidacaoFuncionarioEspecialidadeStrategy | Cenario: validar | Dados: quando funcionario nao encontrado | Verifica: deve lancar")
    @Test
    void validar_deveLancar_quandoFuncionarioNaoEncontrado() {
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> strategy.validar(null, 1L, 1L, 0L)
        );
    }

    private Funcionario funcionarioComServicos(List<Servico> servicos) {
        Especialidade especialidade = new Especialidade();
        especialidade.setServicos(servicos);

        Funcionario funcionario = new Funcionario();
        funcionario.setEspecialidades(List.of(especialidade));
        return funcionario;
    }

    private Servico servico(Long id) {
        Servico servico = new Servico();
        servico.setId(id);
        return servico;
    }
}
