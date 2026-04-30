package sptech.school.backend.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.exception.RegraNegocioException;

@Order(1)
@Component
public class HorarioValidoStrategy implements RegraAgendamentoStrategy {

    @Override
    public void validar(Agendamento agendamento) {

        if (agendamento.getDataHoraInicio() == null || agendamento.getDataHoraFim() == null) {
            throw new RegraNegocioException("Data/hora não pode ser nula");
        }

        if (!agendamento.getDataHoraFim().isAfter(agendamento.getDataHoraInicio())) {
            throw new RegraNegocioException("Data/hora fim deve ser após início");
        }
    }
}