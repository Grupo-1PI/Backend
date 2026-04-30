package sptech.school.backend.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.exception.ConflitoException;
import sptech.school.backend.repository.AgendamentoRepository;

@Order(2)
@Component
public class FuncionarioDisponivelStrategy implements RegraAgendamentoStrategy {

    private final AgendamentoRepository repository;

    public FuncionarioDisponivelStrategy(AgendamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(Agendamento agendamento) {

        boolean conflito = repository
                .existsByFuncionarioIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                        agendamento.getFuncionario().getId(),
                        agendamento.getDataHoraFim(),
                        agendamento.getDataHoraInicio()
                );

        if (conflito) {
            throw new ConflitoException("Funcionário já possui agendamento nesse horário");
        }
    }
}