package sptech.school.backend.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.exception.RegraNegocioException;
import sptech.school.backend.repository.FuncionarioRepository;
import java.util.Objects;

@Order(7)
@Component
public class ValidacaoFuncionarioEspecialidadeStrategy implements RegraAgendamentoStrategy {

    private final FuncionarioRepository funcionarioRepository;

    public ValidacaoFuncionarioEspecialidadeStrategy(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public void validar(Agendamento agendamento, Long funcionarioId, Long servicoId, Long ignorarId) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionario nao encontrado"));

        boolean possuiEspecialidade = funcionario.getEspecialidades()
                .stream()
                .flatMap(especialidade -> especialidade.getServicos().stream())
                .anyMatch(servico -> Objects.equals(servico.getId(), servicoId));

        if (!possuiEspecialidade) {
            throw new RegraNegocioException("Funcionario nao possui especialidade para o servico informado");
        }
    }
}
