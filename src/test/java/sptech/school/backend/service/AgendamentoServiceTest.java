package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.entity.Status;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.AgendamentoRepository;
import sptech.school.backend.repository.ClienteRepository;
import sptech.school.backend.repository.FuncionarioAgendamentoRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.SalaRepository;
import sptech.school.backend.repository.ServicoRepository;
import sptech.school.backend.repository.StatusRepository;
import sptech.school.backend.strategy.RegraAgendamentoStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - AgendamentoService")
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private FuncionarioAgendamentoRepository funcionarioAgendamentoRepository;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private List<RegraAgendamentoStrategy> regras;

    @Mock
    private RegraAgendamentoStrategy regra;

    @InjectMocks
    private AgendamentoService service;

    @DisplayName("Unidade: AgendamentoService | Cenario: criar | Dados: quando valido | Verifica: deve executar strategies e salvar")
    @Test
    void criar_deveExecutarStrategiesESalvar_quandoValido() {
        stubRecursos();
        stubRegras();
        stubSalvar();

        Agendamento resultado = service.criar(agendamento("Criar"), 1L, 1L, 1L, 1L, 1L);

        Assertions.assertEquals(1L, resultado.getId());
        Assertions.assertEquals(1, resultado.getFuncionarioAgendamentos().size());
        Assertions.assertEquals(1, resultado.getAtendimentoServicos().size());
        Mockito.verify(regra).validar(Mockito.any(Agendamento.class), Mockito.eq(1L), Mockito.eq(1L), Mockito.eq(0L));
        Mockito.verify(agendamentoRepository, Mockito.times(2)).save(Mockito.any(Agendamento.class));
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: criar | Dados: quando cliente nao existe | Verifica: deve lancar")
    @Test
    void criar_deveLancar_quandoClienteNaoExiste() {
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.criar(agendamento("Criar"), 1L, 1L, 1L, 1L, 1L)
        );
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: criar | Dados: quando funcionario nao existe | Verifica: deve lancar")
    @Test
    void criar_deveLancar_quandoFuncionarioNaoExiste() {
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(new Cliente()));
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.criar(agendamento("Criar"), 1L, 1L, 1L, 1L, 1L)
        );
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: criar | Dados: quando sala nao existe | Verifica: deve lancar")
    @Test
    void criar_deveLancar_quandoSalaNaoExiste() {
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(new Cliente()));
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario(1L)));
        Mockito.when(salaRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.criar(agendamento("Criar"), 1L, 1L, 1L, 1L, 1L)
        );
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: criar | Dados: quando servico nao existe | Verifica: deve lancar")
    @Test
    void criar_deveLancar_quandoServicoNaoExiste() {
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(new Cliente()));
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario(1L)));
        Mockito.when(salaRepository.findById(1L)).thenReturn(Optional.of(sala(1L)));
        Mockito.when(servicoRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.criar(agendamento("Criar"), 1L, 1L, 1L, 1L, 1L)
        );
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: criar | Dados: quando status nao existe | Verifica: deve lancar")
    @Test
    void criar_deveLancar_quandoStatusNaoExiste() {
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(new Cliente()));
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario(1L)));
        Mockito.when(salaRepository.findById(1L)).thenReturn(Optional.of(sala(1L)));
        Mockito.when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico(1L)));
        Mockito.when(statusRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.criar(agendamento("Criar"), 1L, 1L, 1L, 1L, 1L)
        );
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: listar | Dados: dados preparados no arrange do teste | Verifica: deve retornar todos")
    @Test
    void listar_deveRetornarTodos() {
        List<Agendamento> agendamentos = List.of(agendamento("Lista"));
        Mockito.when(agendamentoRepository.findAll()).thenReturn(agendamentos);

        List<Agendamento> resultado = service.listar();

        Assertions.assertEquals(agendamentos, resultado);
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: listar por periodo | Dados: dados preparados no arrange do teste | Verifica: deve retornar filtrado")
    @Test
    void listarPorPeriodo_deveRetornarFiltrado() {
        LocalDateTime inicio = LocalDateTime.of(2026, 6, 9, 8, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 6, 9, 9, 0);
        List<Agendamento> agendamentos = List.of(agendamento("Periodo"));
        Mockito.when(agendamentoRepository.findByPeriodo(inicio, fim)).thenReturn(agendamentos);

        List<Agendamento> resultado = service.listarPorPeriodo(inicio, fim);

        Assertions.assertEquals(agendamentos, resultado);
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: listar por status | Dados: dados preparados no arrange do teste | Verifica: deve retornar filtrado")
    @Test
    void listarPorStatus_deveRetornarFiltrado() {
        List<Agendamento> agendamentos = List.of(agendamento("Status"));
        Mockito.when(agendamentoRepository.findByStatusId(1L)).thenReturn(agendamentos);

        List<Agendamento> resultado = service.listarPorStatus(1L);

        Assertions.assertEquals(agendamentos, resultado);
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: listar por cliente | Dados: dados preparados no arrange do teste | Verifica: deve retornar filtrado")
    @Test
    void listarPorCliente_deveRetornarFiltrado() {
        List<Agendamento> agendamentos = List.of(agendamento("Cliente"));
        Mockito.when(agendamentoRepository.findByClienteId(1L)).thenReturn(agendamentos);

        List<Agendamento> resultado = service.listarPorCliente(1L);

        Assertions.assertEquals(agendamentos, resultado);
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: buscar por ID | Dados: quando existe | Verifica: deve retornar")
    @Test
    void buscarPorId_deveRetornar_quandoExiste() {
        Agendamento agendamento = agendamento("Busca");
        Mockito.when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        Agendamento resultado = service.buscarPorId(1L);

        Assertions.assertEquals(agendamento, resultado);
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: buscar por ID | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void buscarPorId_deveLancar_quandoNaoExiste() {
        Mockito.when(agendamentoRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(1L));
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: atualizar | Dados: dados preparados no arrange do teste | Verifica: deve revalidar eAtualizar")
    @Test
    void atualizar_deveRevalidarEAtualizar() {
        Agendamento existente = agendamento("Antigo");
        existente.setId(1L);
        stubRecursos();
        stubRegras();
        stubSalvar();
        Mockito.when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(existente));

        Agendamento resultado = service.atualizar(1L, agendamento("Novo"), 1L, 1L, 1L, 1L, 1L);

        Assertions.assertEquals("Novo", resultado.getObservacao());
        Assertions.assertEquals(1, resultado.getFuncionarioAgendamentos().size());
        Assertions.assertEquals(1, resultado.getAtendimentoServicos().size());
        Mockito.verify(regra).validar(Mockito.any(Agendamento.class), Mockito.eq(1L), Mockito.eq(1L), Mockito.eq(1L));
        Mockito.verify(agendamentoRepository).flush();
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: atualizar | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void atualizar_deveLancar_quandoNaoExiste() {
        Mockito.when(agendamentoRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.atualizar(1L, agendamento("Novo"), 1L, 1L, 1L, 1L, 1L)
        );
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: deletar | Dados: dados preparados no arrange do teste | Verifica: deve deletar")
    @Test
    void deletar_deveDeletar() {
        Agendamento agendamento = agendamento("Excluir");
        Mockito.when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        service.deletar(1L);

        Mockito.verify(agendamentoRepository).delete(agendamento);
    }

    @DisplayName("Unidade: AgendamentoService | Cenario: deletar | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void deletar_deveLancar_quandoNaoExiste() {
        Mockito.when(agendamentoRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.deletar(1L));
        Mockito.verify(agendamentoRepository, Mockito.never()).delete(Mockito.any());
    }

    private void stubRecursos() {
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(new Cliente()));
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario(1L)));
        Mockito.when(salaRepository.findById(1L)).thenReturn(Optional.of(sala(1L)));
        Mockito.when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico(1L)));
        Mockito.when(statusRepository.findById(1L)).thenReturn(Optional.of(status(1L)));
    }

    private void stubRegras() {
        Mockito.when(regras.iterator()).thenAnswer(invocation -> List.of(regra).iterator());
    }

    private void stubSalvar() {
        Mockito.when(agendamentoRepository.save(Mockito.any(Agendamento.class))).thenAnswer(invocation -> {
            Agendamento agendamento = invocation.getArgument(0);
            if (agendamento.getId() == null) {
                agendamento.setId(1L);
            }
            return agendamento;
        });
    }

    private Agendamento agendamento(String observacao) {
        Agendamento agendamento = new Agendamento();
        agendamento.setDataHoraInicio(LocalDateTime.of(2026, 6, 9, 8, 0));
        agendamento.setDataHoraFim(LocalDateTime.of(2026, 6, 9, 9, 0));
        agendamento.setObservacao(observacao);
        return agendamento;
    }

    private Funcionario funcionario(Long id) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(id);
        return funcionario;
    }

    private Sala sala(Long id) {
        Sala sala = new Sala();
        sala.setId(id);
        return sala;
    }

    private Servico servico(Long id) {
        Servico servico = new Servico();
        servico.setId(id);
        servico.setValor(new BigDecimal("100.00"));
        servico.setDescricao("Descricao");
        return servico;
    }

    private Status status(Long id) {
        Status status = new Status();
        status.setId(id);
        return status;
    }
}
