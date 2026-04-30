package sptech.school.backend.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.exception.ConflitoException;
import sptech.school.backend.repository.AgendamentoRepository;

@Order(3)
@Component
public class SalaDisponivelStrategy implements RegraAgendamentoStrategy {

    private final AgendamentoRepository repository;

    public SalaDisponivelStrategy(AgendamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(Agendamento agendamento) {

        boolean conflito = repository
                .existsBySalaIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                        agendamento.getSala().getId(),
                        agendamento.getDataHoraFim(),
                        agendamento.getDataHoraInicio()
                );

        if (conflito) {
            throw new ConflitoException("Sala já está ocupada nesse horário");
        }
    }
}