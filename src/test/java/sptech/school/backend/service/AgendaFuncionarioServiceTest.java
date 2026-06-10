package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.dto.AgendaDto.AgendaExcecaoDto;
import sptech.school.backend.dto.AgendaDto.AgendaFuncionarioDto;
import sptech.school.backend.dto.AgendaDto.AgendaFuncionarioListagemDto;
import sptech.school.backend.entity.AgendaExcecao;
import sptech.school.backend.entity.AgendaFuncionario;
import sptech.school.backend.entity.Especialidade;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.AgendaExcecaoRepository;
import sptech.school.backend.repository.AgendaFuncionarioRepository;
import sptech.school.backend.repository.FuncionarioRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - AgendaFuncionarioService")
class AgendaFuncionarioServiceTest {

    @Mock
    private AgendaFuncionarioRepository agendaFuncionarioRepository;

    @Mock
    private AgendaExcecaoRepository agendaExcecaoRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private AgendaFuncionarioService service;

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: listar por funcionario | Dados: dados preparados no arrange do teste | Verifica: deve retornar lista")
    @Test
    void listarPorFuncionario_deveRetornarLista() {
        List<AgendaFuncionario> agendas = List.of(agenda(1L, funcionario(1L), 2));
        Mockito.when(agendaFuncionarioRepository.findByFuncionarioId(1L)).thenReturn(agendas);

        List<AgendaFuncionario> resultado = service.listarPorFuncionario(1L);

        Assertions.assertEquals(agendas, resultado);
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: criar | Dados: dados preparados no arrange do teste | Verifica: deve salvar agenda")
    @Test
    void criar_deveSalvarAgenda() {
        AgendaFuncionarioDto dto = agendaDto(1L, 2, LocalTime.of(8, 0), LocalTime.of(12, 0));
        Funcionario funcionario = funcionario(1L);
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        Mockito.when(agendaFuncionarioRepository.save(Mockito.any(AgendaFuncionario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AgendaFuncionario resultado = service.criar(dto);

        Assertions.assertEquals(funcionario, resultado.getFuncionario());
        Assertions.assertEquals(2, resultado.getDiaSemana());
        Assertions.assertEquals(LocalTime.of(8, 0), resultado.getHoraInicio());
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: criar | Dados: quando funcionario nao existe | Verifica: deve lancar")
    @Test
    void criar_deveLancar_quandoFuncionarioNaoExiste() {
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.criar(agendaDto(1L, 2, null, null)));
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: atualizar | Dados: dados preparados no arrange do teste | Verifica: deve atualizar horarios")
    @Test
    void atualizar_deveAtualizarHorarios() {
        AgendaFuncionario existente = agenda(1L, funcionario(1L), 2);
        Funcionario funcionario = funcionario(2L);
        AgendaFuncionarioDto dto = agendaDto(2L, 3, LocalTime.of(9, 0), LocalTime.of(11, 0));
        Mockito.when(agendaFuncionarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(funcionarioRepository.findById(2L)).thenReturn(Optional.of(funcionario));
        Mockito.when(agendaFuncionarioRepository.save(existente)).thenReturn(existente);

        AgendaFuncionario resultado = service.atualizar(1L, dto);

        Assertions.assertEquals(funcionario, resultado.getFuncionario());
        Assertions.assertEquals(3, resultado.getDiaSemana());
        Assertions.assertEquals(LocalTime.of(9, 0), resultado.getHoraInicio());
        Assertions.assertEquals(LocalTime.of(11, 0), resultado.getHoraFim());
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: atualizar | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void atualizar_deveLancar_quandoNaoExiste() {
        Mockito.when(agendaFuncionarioRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizar(1L, agendaDto(1L, 2, null, null)));
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: deletar | Dados: dados preparados no arrange do teste | Verifica: deve deletar")
    @Test
    void deletar_deveDeletar() {
        Mockito.when(agendaFuncionarioRepository.existsById(1L)).thenReturn(true);

        service.deletar(1L);

        Mockito.verify(agendaFuncionarioRepository).deleteById(1L);
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: deletar | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void deletar_deveLancar_quandoNaoExiste() {
        Mockito.when(agendaFuncionarioRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.deletar(1L));
        Mockito.verify(agendaFuncionarioRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: criar excecao | Dados: dados preparados no arrange do teste | Verifica: deve salvar")
    @Test
    void criarExcecao_deveSalvar() {
        AgendaExcecaoDto dto = excecaoDto(1L);
        Funcionario funcionario = funcionario(1L);
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        Mockito.when(agendaExcecaoRepository.save(Mockito.any(AgendaExcecao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AgendaExcecao resultado = service.criarExcecao(dto);

        Assertions.assertEquals(funcionario, resultado.getFuncionario());
        Assertions.assertEquals(LocalDate.of(2026, 6, 9), resultado.getData());
        Assertions.assertFalse(resultado.getDisponivel());
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: criar excecao | Dados: quando funcionario nao existe | Verifica: deve lancar")
    @Test
    void criarExcecao_deveLancar_quandoFuncionarioNaoExiste() {
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.criarExcecao(excecaoDto(1L)));
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: deletar excecao | Dados: dados preparados no arrange do teste | Verifica: deve deletar")
    @Test
    void deletarExcecao_deveDeletar() {
        Mockito.when(agendaExcecaoRepository.existsById(1L)).thenReturn(true);

        service.deletarExcecao(1L);

        Mockito.verify(agendaExcecaoRepository).deleteById(1L);
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: deletar excecao | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void deletarExcecao_deveLancar_quandoNaoExiste() {
        Mockito.when(agendaExcecaoRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.deletarExcecao(1L));
        Mockito.verify(agendaExcecaoRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: listar excecoes por funcionario | Dados: dados preparados no arrange do teste | Verifica: deve retornar lista")
    @Test
    void listarExcecoesPorFuncionario_deveRetornarLista() {
        List<AgendaExcecao> excecoes = List.of(new AgendaExcecao());
        Mockito.when(agendaExcecaoRepository.findByFuncionarioId(1L)).thenReturn(excecoes);

        List<AgendaExcecao> resultado = service.listarExcecoesPorFuncionario(1L);

        Assertions.assertEquals(excecoes, resultado);
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: listar todos com agenda | Dados: dados preparados no arrange do teste | Verifica: deve retornar lista agrupada")
    @Test
    void listarTodosComAgenda_deveRetornarListaAgrupada() {
        Funcionario funcionario = funcionario(1L);
        Especialidade especialidade = new Especialidade();
        especialidade.setNome("Acupuntura");
        funcionario.setEspecialidades(List.of(especialidade));
        AgendaFuncionario agenda = agenda(10L, funcionario, 2);
        Mockito.when(funcionarioRepository.findAll()).thenReturn(List.of(funcionario));
        Mockito.when(agendaFuncionarioRepository.findByFuncionarioId(1L)).thenReturn(List.of(agenda));

        List<AgendaFuncionarioListagemDto> resultado = service.listarTodosComAgenda();

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(1L, resultado.get(0).getFuncionarioId());
        Assertions.assertEquals("Funcionario 1", resultado.get(0).getFuncionarioNome());
        Assertions.assertEquals("Acupuntura", resultado.get(0).getEspecialidadePrincipal());
        Assertions.assertEquals(1, resultado.get(0).getAgendas().size());
    }

    @DisplayName("Unidade: AgendaFuncionarioService | Cenario: listar todos com agenda | Dados: quando funcionario sem especialidades | Verifica: deve retornar especialidade nula")
    @Test
    void listarTodosComAgenda_deveRetornarEspecialidadeNula_quandoFuncionarioSemEspecialidades() {
        Funcionario funcionario = funcionario(1L);
        funcionario.setEspecialidades(List.of());
        Mockito.when(funcionarioRepository.findAll()).thenReturn(List.of(funcionario));
        Mockito.when(agendaFuncionarioRepository.findByFuncionarioId(1L)).thenReturn(List.of());

        List<AgendaFuncionarioListagemDto> resultado = service.listarTodosComAgenda();

        Assertions.assertNull(resultado.get(0).getEspecialidadePrincipal());
        Assertions.assertTrue(resultado.get(0).getAgendas().isEmpty());
    }

    private AgendaFuncionarioDto agendaDto(Long funcionarioId, Integer diaSemana, LocalTime inicio, LocalTime fim) {
        AgendaFuncionarioDto dto = new AgendaFuncionarioDto();
        dto.setFuncionarioId(funcionarioId);
        dto.setDiaSemana(diaSemana);
        dto.setHoraInicio(inicio);
        dto.setHoraFim(fim);
        return dto;
    }

    private AgendaExcecaoDto excecaoDto(Long funcionarioId) {
        AgendaExcecaoDto dto = new AgendaExcecaoDto();
        dto.setFuncionarioId(funcionarioId);
        dto.setData(LocalDate.of(2026, 6, 9));
        dto.setHoraInicio(LocalTime.of(8, 0));
        dto.setHoraFim(LocalTime.of(12, 0));
        dto.setDisponivel(false);
        return dto;
    }

    private AgendaFuncionario agenda(Long id, Funcionario funcionario, Integer diaSemana) {
        AgendaFuncionario agenda = new AgendaFuncionario();
        agenda.setId(id);
        agenda.setFuncionario(funcionario);
        agenda.setDiaSemana(diaSemana);
        agenda.setHoraInicio(LocalTime.of(8, 0));
        agenda.setHoraFim(LocalTime.of(12, 0));
        return agenda;
    }

    private Funcionario funcionario(Long id) {
        Usuario usuario = new Usuario();
        usuario.setNome("Funcionario " + id);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(id);
        funcionario.setUsuario(usuario);
        return funcionario;
    }
}
