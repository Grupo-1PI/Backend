package sptech.school.backend.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.exception.RegraNegocioException;
import sptech.school.backend.repository.ServicoRepository;
import java.util.Objects;

@Order(6)
@Component
public class ValidacaoSalaServicoStrategy implements RegraAgendamentoStrategy {

    private final ServicoRepository servicoRepository;

    public ValidacaoSalaServicoStrategy(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @Override
    public void validar(Agendamento agendamento, Long funcionarioId, Long servicoId, Long ignorarId) {
        Servico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Servico nao encontrado"));

        boolean salaAtendeServico = servico.getSalas()
                .stream()
                .anyMatch(sala -> Objects.equals(sala.getId(), agendamento.getSala().getId()));

        if (!salaAtendeServico) {
            throw new RegraNegocioException("Sala nao atende o servico informado");
        }
    }
}
