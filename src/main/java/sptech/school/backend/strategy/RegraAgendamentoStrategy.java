package sptech.school.backend.strategy;

import sptech.school.backend.entity.Agendamento;

public interface RegraAgendamentoStrategy {
    void validar(Agendamento agendamento, Long funcionarioId, Long servicoId, Long ignorarId);
}
