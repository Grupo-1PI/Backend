package sptech.school.backend.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sptech.school.backend.entity.AgendaExcecao;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.exception.ConflitoException;
import sptech.school.backend.repository.AgendaExcecaoRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Order(5)
@Component
public class ValidacaoExcecaoAgendaStrategy implements RegraAgendamentoStrategy {

    private final AgendaExcecaoRepository agendaExcecaoRepository;

    public ValidacaoExcecaoAgendaStrategy(AgendaExcecaoRepository agendaExcecaoRepository) {
        this.agendaExcecaoRepository = agendaExcecaoRepository;
    }

    @Override
    public void validar(Agendamento agendamento, Long funcionarioId, Long servicoId, Long ignorarId) {
        LocalDate data = agendamento.getDataHoraInicio().toLocalDate();
        LocalTime inicio = agendamento.getDataHoraInicio().toLocalTime();
        LocalTime fim = agendamento.getDataHoraFim().toLocalTime();

        List<AgendaExcecao> excecoes = agendaExcecaoRepository.findByFuncionarioIdAndData(funcionarioId, data);

        for (AgendaExcecao excecao : excecoes) {
            if (Boolean.FALSE.equals(excecao.getDisponivel()) && bloqueiaHorario(excecao, inicio, fim)) {
                throw new ConflitoException("Funcionario indisponivel nesse periodo");
            }
        }
    }

    private boolean bloqueiaHorario(AgendaExcecao excecao, LocalTime inicio, LocalTime fim) {
        if (excecao.getHoraInicio() == null || excecao.getHoraFim() == null) {
            return true;
        }

        return inicio.isBefore(excecao.getHoraFim()) && fim.isAfter(excecao.getHoraInicio());
    }
}
