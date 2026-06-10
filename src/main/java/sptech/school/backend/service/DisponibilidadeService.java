package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.backend.dto.DisponibilidadeDto.DiaDisponivelDto;
import sptech.school.backend.dto.DisponibilidadeDto.HorarioDisponivelDto;
import sptech.school.backend.dto.DisponibilidadeDto.SalaDisponibilidadeDto;
import sptech.school.backend.entity.AgendaFuncionario;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.repository.AgendaExcecaoRepository;
import sptech.school.backend.repository.AgendaFuncionarioRepository;
import sptech.school.backend.repository.AgendamentoRepository;
import sptech.school.backend.repository.SalaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DisponibilidadeService {

    private static final DateTimeFormatter HORA_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final AgendamentoRepository agendamentoRepository;
    private final AgendaFuncionarioRepository agendaFuncionarioRepository;
    private final AgendaExcecaoRepository agendaExcecaoRepository;
    private final SalaRepository salaRepository;

    public DisponibilidadeService(
            AgendamentoRepository agendamentoRepository,
            AgendaFuncionarioRepository agendaFuncionarioRepository,
            AgendaExcecaoRepository agendaExcecaoRepository,
            SalaRepository salaRepository
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendaFuncionarioRepository = agendaFuncionarioRepository;
        this.agendaExcecaoRepository = agendaExcecaoRepository;
        this.salaRepository = salaRepository;
    }

    @Transactional(readOnly = true)
    public List<DiaDisponivelDto> calcularCalendario(String mes) {
        YearMonth yearMonth = YearMonth.parse(mes);
        List<AgendaFuncionario> agendas = agendaFuncionarioRepository.findAll();
        List<DiaDisponivelDto> dias = new ArrayList<>();

        for (int dia = 1; dia <= yearMonth.lengthOfMonth(); dia++) {
            LocalDate data = yearMonth.atDay(dia);
            Integer diaSemana = obterDiaSemanaMySql(data);
            long vagasTotais = agendas.stream()
                    .filter(agenda -> Objects.equals(agenda.getDiaSemana(), diaSemana))
                    .mapToLong(this::calcularVagasDaFaixa)
                    .sum();

            long agendamentos = agendamentoRepository.findByPeriodo(
                    data.atStartOfDay(),
                    data.plusDays(1).atStartOfDay()
            ).size();

            dias.add(new DiaDisponivelDto(data.toString(), calcularStatus(vagasTotais, agendamentos)));
        }

        return dias;
    }

    @Transactional(readOnly = true)
    public List<HorarioDisponivelDto> calcularHorariosDisponiveis(LocalDate data) {
        Integer diaSemana = obterDiaSemanaMySql(data);
        List<HorarioDisponivelDto> horarios = new ArrayList<>();

        agendaFuncionarioRepository.findAll().stream()
                .filter(agenda -> Objects.equals(agenda.getDiaSemana(), diaSemana))
                .forEach(agenda -> adicionarHorariosDaFaixa(data, agenda, horarios));

        Map<String, HorarioDisponivelDto> mapa = new LinkedHashMap<>();
        for (HorarioDisponivelDto h : horarios) {
            mapa.merge(h.getHorario(), h, (existente, novo) ->
                    Boolean.FALSE.equals(novo.isDisponivel()) ? novo : existente
            );
        }
        List<HorarioDisponivelDto> horariosDeduplicados = new ArrayList<>(mapa.values());
        horariosDeduplicados.sort(Comparator.comparing(HorarioDisponivelDto::getHorario));
        return horariosDeduplicados;
    }

    @Transactional(readOnly = true)
    public List<SalaDisponibilidadeDto> verificarDisponibilidadeSalas(LocalDateTime inicio, LocalDateTime fim) {
        List<SalaDisponibilidadeDto> salasDisponibilidade = new ArrayList<>();

        for (Sala sala : salaRepository.findAll()) {
            boolean ocupada = agendamentoRepository.existeConflitoSala(sala.getId(), inicio, fim, 0L);
            salasDisponibilidade.add(new SalaDisponibilidadeDto(sala.getId(), sala.getDescricao(), ocupada));
        }

        return salasDisponibilidade;
    }

    private void adicionarHorariosDaFaixa(
            LocalDate data,
            AgendaFuncionario agenda,
            List<HorarioDisponivelDto> horarios
    ) {
        LocalTime horario = agenda.getHoraInicio();

        while (horario != null && agenda.getHoraFim() != null && !horario.plusHours(1).isAfter(agenda.getHoraFim())) {
            LocalDateTime inicio = LocalDateTime.of(data, horario);
            LocalDateTime fim = inicio.plusHours(1);
            boolean possuiAgendamento = !agendamentoRepository.findByPeriodo(inicio, fim).isEmpty();

            horarios.add(new HorarioDisponivelDto(horario.format(HORA_FORMATTER), !possuiAgendamento));
            horario = horario.plusHours(1);
        }
    }

    private long calcularVagasDaFaixa(AgendaFuncionario agenda) {
        long vagas = 0;
        LocalTime horario = agenda.getHoraInicio();

        while (horario != null && agenda.getHoraFim() != null && !horario.plusHours(1).isAfter(agenda.getHoraFim())) {
            vagas++;
            horario = horario.plusHours(1);
        }

        return vagas;
    }

    private String calcularStatus(long vagasTotais, long agendamentos) {
        if (vagasTotais <= 0 || agendamentos >= vagasTotais) {
            return "indisponivel";
        }

        if (vagasTotais - agendamentos <= 2) {
            return "poucos_horarios";
        }

        return "disponivel";
    }

    private Integer obterDiaSemanaMySql(LocalDate data) {
        return data.getDayOfWeek().getValue() % 7 + 1;
    }
}
