package sptech.school.backend.strategy;

import org.springframework.stereotype.Component;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.exception.HorarioIndisponivelException;
import sptech.school.backend.repository.AgendamentoRepository;

@Component
public class HorarioDisponivelStrategy implements RegraAgendamentoStrategy {

    private final AgendamentoRepository repository;

    public HorarioDisponivelStrategy(AgendamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(Agendamento agendamento) {

        boolean ocupado;

        if (agendamento.getId() == null) {
            ocupado = repository.existsByDataHora(agendamento.getDataHora());
        } else {
            ocupado = repository.existsByDataHoraAndIdNot(
                    agendamento.getDataHora(),
                    agendamento.getId()
            );
        }

        if (ocupado) {
            throw new HorarioIndisponivelException();
        }
    }
}