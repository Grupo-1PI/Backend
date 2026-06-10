package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.dto.DisponibilidadeDto.DiaDisponivelDto;
import sptech.school.backend.dto.DisponibilidadeDto.HorarioDisponivelDto;
import sptech.school.backend.dto.DisponibilidadeDto.SalaDisponibilidadeDto;
import sptech.school.backend.entity.AgendaFuncionario;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.repository.AgendaExcecaoRepository;
import sptech.school.backend.repository.AgendaFuncionarioRepository;
import sptech.school.backend.repository.AgendamentoRepository;
import sptech.school.backend.repository.SalaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - DisponibilidadeService")
class DisponibilidadeServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private AgendaFuncionarioRepository agendaFuncionarioRepository;

    @Mock
    private AgendaExcecaoRepository agendaExcecaoRepository;

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private DisponibilidadeService service;

    @DisplayName("Unidade: DisponibilidadeService | Cenario: calcular calendario | Dados: dados preparados no arrange do teste | Verifica: deve retornar dias do mes")
    @Test
    void calcularCalendario_deveRetornarDiasDoMes() {
        Mockito.when(agendaFuncionarioRepository.findAll()).thenReturn(List.of(agenda(2, 8, 12)));
        Mockito.when(agendamentoRepository.findByPeriodo(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of());

        List<DiaDisponivelDto> resultado = service.calcularCalendario("2026-06");

        Assertions.assertEquals(30, resultado.size());
    }

    @DisplayName("Unidade: DisponibilidadeService | Cenario: calcular calendario | Dados: dados preparados no arrange do teste | Verifica: dias sem agenda; sao indisponiveis")
    @Test
    void calcularCalendario_diasSemAgenda_saoIndisponiveis() {
        Mockito.when(agendaFuncionarioRepository.findAll()).thenReturn(List.of());
        Mockito.when(agendamentoRepository.findByPeriodo(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of());

        List<DiaDisponivelDto> resultado = service.calcularCalendario("2026-06");

        Assertions.assertTrue(resultado.stream().allMatch(dia -> "indisponivel".equals(dia.getStatus())));
    }

    @DisplayName("Unidade: DisponibilidadeService | Cenario: calcular calendario | Dados: quando restam ate duas vagas | Verifica: deve retornar poucos horarios")
    @Test
    void calcularCalendario_deveRetornarPoucosHorarios_quandoRestamAteDuasVagas() {
        Mockito.when(agendaFuncionarioRepository.findAll()).thenReturn(List.of(agenda(2, 8, 12)));
        Mockito.when(agendamentoRepository.findByPeriodo(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> {
                    LocalDateTime inicio = invocation.getArgument(0);
                    if (inicio.toLocalDate().equals(LocalDate.of(2026, 6, 8))) {
                        return List.of(new Agendamento(), new Agendamento());
                    }
                    return List.of();
                });

        List<DiaDisponivelDto> resultado = service.calcularCalendario("2026-06");

        Assertions.assertEquals("poucos_horarios", resultado.get(7).getStatus());
    }

    @DisplayName("Unidade: DisponibilidadeService | Cenario: calcular calendario | Dados: quando agendamentos preenchem todas as vagas | Verifica: deve retornar indisponivel")
    @Test
    void calcularCalendario_deveRetornarIndisponivel_quandoAgendamentosPreenchemTodasAsVagas() {
        Mockito.when(agendaFuncionarioRepository.findAll()).thenReturn(List.of(agenda(2, 8, 12)));
        Mockito.when(agendamentoRepository.findByPeriodo(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> {
                    LocalDateTime inicio = invocation.getArgument(0);
                    if (inicio.toLocalDate().equals(LocalDate.of(2026, 6, 8))) {
                        return List.of(new Agendamento(), new Agendamento(), new Agendamento(), new Agendamento());
                    }
                    return List.of();
                });

        List<DiaDisponivelDto> resultado = service.calcularCalendario("2026-06");

        Assertions.assertEquals("indisponivel", resultado.get(7).getStatus());
    }

    @DisplayName("Unidade: DisponibilidadeService | Cenario: calcular calendario | Dados: dados preparados no arrange do teste | Verifica: deve tratar agenda sem hora inicio ou fim")
    @Test
    void calcularCalendario_deveTratarAgendaSemHoraInicioOuFim() {
        AgendaFuncionario semInicio = new AgendaFuncionario();
        semInicio.setDiaSemana(2);
        semInicio.setHoraInicio(null);
        semInicio.setHoraFim(LocalTime.of(12, 0));
        AgendaFuncionario semFim = new AgendaFuncionario();
        semFim.setDiaSemana(2);
        semFim.setHoraInicio(LocalTime.of(8, 0));
        semFim.setHoraFim(null);
        Mockito.when(agendaFuncionarioRepository.findAll()).thenReturn(List.of(semInicio, semFim));
        Mockito.when(agendamentoRepository.findByPeriodo(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of());

        List<DiaDisponivelDto> resultado = service.calcularCalendario("2026-06");

        Assertions.assertEquals("indisponivel", resultado.get(7).getStatus());
    }

    @DisplayName("Unidade: DisponibilidadeService | Cenario: calcular horarios disponiveis | Dados: dados preparados no arrange do teste | Verifica: deve retornar slots")
    @Test
    void calcularHorariosDisponiveis_deveRetornarSlots() {
        Mockito.when(agendaFuncionarioRepository.findAll()).thenReturn(List.of(agenda(2, 8, 10)));
        Mockito.when(agendamentoRepository.findByPeriodo(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of());

        List<HorarioDisponivelDto> resultado = service.calcularHorariosDisponiveis(LocalDate.of(2026, 6, 8));

        Assertions.assertEquals(List.of("08:00", "09:00"), resultado.stream().map(HorarioDisponivelDto::getHorario).toList());
    }

    @DisplayName("Unidade: DisponibilidadeService | Cenario: calcular horarios disponiveis | Dados: quando dois funcionarios no dia | Verifica: deve deduplicar")
    @Test
    void calcularHorariosDisponiveis_deveDeduplicar_quandoDoisFuncionariosNoDia() {
        Mockito.when(agendaFuncionarioRepository.findAll()).thenReturn(List.of(agenda(2, 8, 10), agenda(2, 8, 10)));
        Mockito.when(agendamentoRepository.findByPeriodo(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(), List.of(), List.of(new Agendamento()), List.of());

        List<HorarioDisponivelDto> resultado = service.calcularHorariosDisponiveis(LocalDate.of(2026, 6, 8));

        Assertions.assertEquals(2, resultado.size());
        Assertions.assertEquals("08:00", resultado.get(0).getHorario());
        Assertions.assertFalse(resultado.get(0).isDisponivel());
        Assertions.assertEquals("09:00", resultado.get(1).getHorario());
    }

    @DisplayName("Unidade: DisponibilidadeService | Cenario: calcular horarios disponiveis | Dados: quando agenda sem hora inicio ou fim | Verifica: deve retornar vazio")
    @Test
    void calcularHorariosDisponiveis_deveRetornarVazio_quandoAgendaSemHoraInicioOuFim() {
        AgendaFuncionario semInicio = new AgendaFuncionario();
        semInicio.setDiaSemana(2);
        semInicio.setHoraFim(LocalTime.of(10, 0));
        AgendaFuncionario semFim = new AgendaFuncionario();
        semFim.setDiaSemana(2);
        semFim.setHoraInicio(LocalTime.of(8, 0));
        Mockito.when(agendaFuncionarioRepository.findAll()).thenReturn(List.of(semInicio, semFim));

        List<HorarioDisponivelDto> resultado = service.calcularHorariosDisponiveis(LocalDate.of(2026, 6, 8));

        Assertions.assertTrue(resultado.isEmpty());
    }

    @DisplayName("Unidade: DisponibilidadeService | Cenario: verificar disponibilidade salas | Dados: dados preparados no arrange do teste | Verifica: deve retornar status correto")
    @Test
    void verificarDisponibilidadeSalas_deveRetornarStatusCorreto() {
        Sala sala1 = sala(1L, "Sala 1");
        Sala sala2 = sala(2L, "Sala 2");
        LocalDateTime inicio = LocalDateTime.of(2026, 6, 9, 8, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 6, 9, 9, 0);
        Mockito.when(salaRepository.findAll()).thenReturn(List.of(sala1, sala2));
        Mockito.when(agendamentoRepository.existeConflitoSala(1L, inicio, fim, 0L)).thenReturn(true);
        Mockito.when(agendamentoRepository.existeConflitoSala(2L, inicio, fim, 0L)).thenReturn(false);

        List<SalaDisponibilidadeDto> resultado = service.verificarDisponibilidadeSalas(inicio, fim);

        Assertions.assertTrue(resultado.get(0).isOcupada());
        Assertions.assertFalse(resultado.get(1).isOcupada());
    }

    private AgendaFuncionario agenda(Integer diaSemana, int inicio, int fim) {
        AgendaFuncionario agenda = new AgendaFuncionario();
        agenda.setDiaSemana(diaSemana);
        agenda.setHoraInicio(LocalTime.of(inicio, 0));
        agenda.setHoraFim(LocalTime.of(fim, 0));
        return agenda;
    }

    private Sala sala(Long id, String descricao) {
        Sala sala = new Sala();
        sala.setId(id);
        sala.setDescricao(descricao);
        return sala;
    }
}
