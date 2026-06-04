package sptech.school.backend.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sptech.school.backend.entity.AgendaFuncionario;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.exception.RegraNegocioException;
import sptech.school.backend.repository.AgendaFuncionarioRepository;
import java.time.LocalTime;
import java.util.List;

@Order(4)
@Component
public class ValidacaoHorarioTrabalhoStrategy implements RegraAgendamentoStrategy {

    private final AgendaFuncionarioRepository agendaFuncionarioRepository;

    public ValidacaoHorarioTrabalhoStrategy(AgendaFuncionarioRepository agendaFuncionarioRepository) {
        this.agendaFuncionarioRepository = agendaFuncionarioRepository;
    }

    @Override
    public void validar(Agendamento agendamento, Long funcionarioId, Long servicoId, Long ignorarId) {
        Integer diaSemana = converterParaDiaSemanaMysql(agendamento.getDataHoraInicio().getDayOfWeek().getValue());
        LocalTime inicio = agendamento.getDataHoraInicio().toLocalTime();
        LocalTime fim = agendamento.getDataHoraFim().toLocalTime();

        List<AgendaFuncionario> agendas = agendaFuncionarioRepository.findByFuncionarioId(funcionarioId);

        boolean dentroDoHorario = agendas.stream()
                .anyMatch(agenda -> agenda.getDiaSemana().equals(diaSemana)
                        && !inicio.isBefore(agenda.getHoraInicio())
                        && !fim.isAfter(agenda.getHoraFim()));

        if (!dentroDoHorario) {
            throw new RegraNegocioException("Funcionario nao possui horario de trabalho para esse periodo");
        }
    }

    private Integer converterParaDiaSemanaMysql(int dayOfWeekJava) {
        return dayOfWeekJava == 7 ? 1 : dayOfWeekJava + 1;
    }
}
