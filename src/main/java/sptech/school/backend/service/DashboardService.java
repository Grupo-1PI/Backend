package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import sptech.school.backend.dto.DashboardDto.AgendamentoDiasSemanaDto;
import sptech.school.backend.dto.DashboardDto.ServicoDadosDto;
import sptech.school.backend.repository.AgendamentoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final AgendamentoRepository agendamentoRepository;

    public DashboardService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public Integer totalAgendamentosAtivos(LocalDate inicio, LocalDate fim) {
        return agendamentoRepository.findByTotalAgendamentos(inicio.atStartOfDay(), fim.atStartOfDay());
    }

    public List<ServicoDadosDto> quantidadeServicosUltimosDias(
            LocalDate inicio,
            LocalDate fim
    ) {
        return agendamentoRepository.findByQuantidadeDeCadaServico(
                inicio.atStartOfDay(),
                fim.atTime(23,59,59)
        );
    }

    public List<AgendamentoDiasSemanaDto> buscarPorDiaSemana(LocalDate inicio, LocalDate fim) {

        List<AgendamentoDiasSemanaDto> resultado =
                agendamentoRepository.findByTotalAtendimentoDiaSemana(inicio.atStartOfDay(), fim.atTime(23, 59, 59));

        Map<String, Long> mapa = resultado.stream()
                .collect(Collectors.toMap(
                        AgendamentoDiasSemanaDto::getDiaSemana,
                        AgendamentoDiasSemanaDto::getTotal,
                        (a, b) -> a
                ));

        return List.of(
                new AgendamentoDiasSemanaDto("Sunday", mapa.getOrDefault("Sunday", 0L)),
                new AgendamentoDiasSemanaDto("Monday", mapa.getOrDefault("Monday", 0L)),
                new AgendamentoDiasSemanaDto("Tuesday", mapa.getOrDefault("Tuesday", 0L)),
                new AgendamentoDiasSemanaDto("Wednesday", mapa.getOrDefault("Wednesday", 0L)),
                new AgendamentoDiasSemanaDto("Thursday", mapa.getOrDefault("Thursday", 0L)),
                new AgendamentoDiasSemanaDto("Friday", mapa.getOrDefault("Friday", 0L)),
                new AgendamentoDiasSemanaDto("Saturday", mapa.getOrDefault("Saturday", 0L))
        );
    }

    public Integer totalCancelamentos(LocalDate inicio, LocalDate fim) {
        return agendamentoRepository.findByTotalCancelamentos(inicio.atStartOfDay(), fim.atTime(23, 59, 59));
    }

    public Integer clientesAtivos(LocalDate inicio, LocalDate fim) {
        return agendamentoRepository.findByClienteAtivosNoPeriodo(inicio.atStartOfDay(), fim.atTime(23, 59, 59));
    }

    public Integer clientesNovos(LocalDate inicio) {
        return agendamentoRepository.findByClientesNovosPeriodo(inicio.atStartOfDay());
    }
}