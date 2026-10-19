package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import sptech.school.backend.dto.DashboardDto.AgendamentoDiasSemanaDto;
import sptech.school.backend.dto.DashboardDto.ServicoDadosDto;
import sptech.school.backend.dto.DashboardDto.RetornoPacientesDto;
import sptech.school.backend.repository.AgendamentoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DashboardService {

    private final AgendamentoRepository agendamentoRepository;

    public DashboardService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public Integer totalAgendamentosAtivos(LocalDate inicio, LocalDate fim) {
        return agendamentoRepository.findByTotalAgendamentos(inicio.atStartOfDay(), fim.atTime(23, 59, 59));
    }

    public List<ServicoDadosDto> quantidadeServicosUltimosDias(
            LocalDate inicio,
            LocalDate fim
    ) {
        return agendamentoRepository.findByQuantidadeDeCadaServico(
                inicio.atStartOfDay(),
                fim.atTime(23, 59, 59)
        );
    }

    public List<AgendamentoDiasSemanaDto> buscarPorDiaSemana(LocalDate inicio, LocalDate fim) {

        List<AgendamentoDiasSemanaDto> resultado = agendamentoRepository.findByTotalAtendimentoDiaSemana(
                inicio.atStartOfDay(), fim.atTime(23, 59, 59));

        String[] dias = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        long[] totais = new long[7];

        for (AgendamentoDiasSemanaDto dto : resultado) {
            int dia = dto.getDiaSemana();
            long total = dto.getTotal();
            if (dia >= 1 && dia <= 7 && totais[dia - 1] == 0) {
                totais[dia - 1] = total;
            }
        }

        return java.util.stream.IntStream.range(0, 7)
                .mapToObj(i -> new AgendamentoDiasSemanaDto(i + 1, totais[i]))
                .toList();
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

    public Long novosPacientes(LocalDate inicio, LocalDate fim) {
        return agendamentoRepository.findByNovosPacientes(
                inicio.atStartOfDay(),
                fim.atTime(23, 59, 59)
        );
    }

    public List<RetornoPacientesDto> retornoPacientes(LocalDate inicio, LocalDate fim) {
        LocalDateTime fimPeriodo = fim.atTime(23, 59, 59);

        List<Object[]> primeiros = agendamentoRepository.findByPrimeiroAtendimentoFinalizado(fimPeriodo);

        List<Object[]> atendimentos = agendamentoRepository.findByAtendimentosFinalizadosNoPeriodo(
                inicio.atStartOfDay(), fimPeriodo
        );

        Map<Long, LocalDateTime> primeiroAtendimento = new HashMap<>();

        for (Object[] linha : primeiros) {
            primeiroAtendimento.put(
                    ((Number) linha[0]).longValue(),
                    (LocalDateTime) linha[1]
            );
        }

        Map<LocalDate, Set<Long>> pacientesPorSemana = new HashMap<>();

        for (Object[] linha : atendimentos) {
            Long clienteId = ((Number) linha[0]).longValue();
            LocalDate data = ((LocalDateTime) linha[1]).toLocalDate();
            LocalDate semana = data.minusDays(data.getDayOfWeek().getValue() % 7);

            pacientesPorSemana
                    .computeIfAbsent(semana, chave -> new HashSet<>())
                    .add(clienteId);
        }

        List<RetornoPacientesDto> resultado = new ArrayList<>();

        LocalDate semana = inicio.minusDays(inicio.getDayOfWeek().getValue() % 7);

        while (!semana.isAfter(fim)) {
            Set<Long> pacientes = pacientesPorSemana.getOrDefault(semana, Set.of());

            LocalDate inicioSemana = semana;

            long retornantes = pacientes.stream()
                    .filter(id -> primeiroAtendimento.get(id).isBefore(inicioSemana.atStartOfDay()))
                    .count();

            double taxa = pacientes.isEmpty()
                    ? 0.0
                    : retornantes * 100.0 / pacientes.size();

            LocalDate primeiroDia = semana.isBefore(inicio) ? inicio : semana;
            LocalDate ultimoDia = semana.plusDays(6).isAfter(fim) ? fim : semana.plusDays(6);

            resultado.add(new RetornoPacientesDto(
                    primeiroDia,
                    ultimoDia,
                    (long) pacientes.size(),
                    retornantes,
                    taxa
            ));

            semana = semana.plusWeeks(1);
        }

        return resultado;
    }
}