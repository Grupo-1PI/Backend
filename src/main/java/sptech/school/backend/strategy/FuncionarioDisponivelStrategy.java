package sptech.school.backend.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.exception.ConflitoException;
import sptech.school.backend.repository.FuncionarioAgendamentoRepository;

@Order(3)
@Component
public class FuncionarioDisponivelStrategy implements RegraAgendamentoStrategy {

    private final FuncionarioAgendamentoRepository repository;

    public FuncionarioDisponivelStrategy(FuncionarioAgendamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(Agendamento agendamento, Long funcionarioId, Long servicoId, Long ignorarId) {

        boolean conflito = repository.existeConflitoFuncionario(
                funcionarioId,
                agendamento.getDataHoraInicio(),
                agendamento.getDataHoraFim(),
                ignorarId
        );

        if (conflito) {
            throw new ConflitoException("Funcionário já possui agendamento nesse horário");
        }
    }
}
