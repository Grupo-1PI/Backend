package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.entity.AtendimentoServico;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.FuncionarioAgendamento;
import sptech.school.backend.entity.FuncionarioAgendamentoId;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.entity.Status;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.AgendamentoRepository;
import sptech.school.backend.repository.ClienteRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.SalaRepository;
import sptech.school.backend.repository.ServicoRepository;
import sptech.school.backend.repository.StatusRepository;
import sptech.school.backend.strategy.RegraAgendamentoStrategy;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final SalaRepository salaRepository;
    private final ServicoRepository servicoRepository;
    private final StatusRepository statusRepository;
    private final List<RegraAgendamentoStrategy> regras;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            ClienteRepository clienteRepository,
            FuncionarioRepository funcionarioRepository,
            SalaRepository salaRepository,
            ServicoRepository servicoRepository,
            StatusRepository statusRepository,
            List<RegraAgendamentoStrategy> regras
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.salaRepository = salaRepository;
        this.servicoRepository = servicoRepository;
        this.statusRepository = statusRepository;
        this.regras = regras;
    }

    @Transactional
    public Agendamento criar(
            Agendamento agendamento,
            Long clienteId,
            Long funcionarioId,
            Long salaId,
            Long servicoId,
            Long statusId
    ) {
        Cliente cliente = buscarCliente(clienteId);
        Funcionario funcionario = buscarFuncionario(funcionarioId);
        Sala sala = buscarSala(salaId);
        Servico servico = buscarServico(servicoId);
        Status status = buscarStatus(statusId);

        agendamento.setCliente(cliente);
        agendamento.setSala(sala);
        agendamento.setStatus(status);

        validarRegras(agendamento, funcionarioId, servicoId, 0L);

        Agendamento salvo = agendamentoRepository.save(agendamento);
        vincularFuncionario(salvo, funcionario);
        vincularServico(salvo, servico);

        return agendamentoRepository.save(salvo);
    }

    public List<Agendamento> listar() {
        return agendamentoRepository.findAll();
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento nao encontrado"));
    }

    public List<Agendamento> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.findByPeriodo(inicio, fim);
    }

    public List<Agendamento> listarPorStatus(Long statusId) {
        return agendamentoRepository.findByStatusId(statusId);
    }

    public List<Agendamento> listarPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId);
    }

    public boolean clientePertenceAoUsuario(Long clienteId, String email) {
        return clienteRepository.findById(clienteId)
                .map(cliente -> cliente.getUsuario().getEmail().equalsIgnoreCase(email))
                .orElse(false);
    }

    @Transactional
    public Agendamento atualizar(
            Long id,
            Agendamento novo,
            Long clienteId,
            Long funcionarioId,
            Long salaId,
            Long servicoId,
            Long statusId
    ) {
        Agendamento existente = buscarPorId(id);
        Cliente cliente = buscarCliente(clienteId);
        Funcionario funcionario = buscarFuncionario(funcionarioId);
        Sala sala = buscarSala(salaId);
        Servico servico = buscarServico(servicoId);
        Status status = buscarStatus(statusId);

        existente.setDataHoraInicio(novo.getDataHoraInicio());
        existente.setDataHoraFim(novo.getDataHoraFim());
        existente.setObservacao(novo.getObservacao());
        existente.setCliente(cliente);
        existente.setSala(sala);
        existente.setStatus(status);

        validarRegras(existente, funcionarioId, servicoId, id);

        existente.getFuncionarioAgendamentos().clear();
        existente.getAtendimentoServicos().clear();
        agendamentoRepository.flush();

        vincularFuncionario(existente, funcionario);
        vincularServico(existente, servico);

        return agendamentoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Agendamento agendamento = buscarPorId(id);
        agendamentoRepository.delete(agendamento);
    }

    private void validarRegras(Agendamento agendamento, Long funcionarioId, Long servicoId, Long ignorarId) {
        for (RegraAgendamentoStrategy regra : regras) {
            regra.validar(agendamento, funcionarioId, servicoId, ignorarId);
        }
    }

    private void vincularFuncionario(Agendamento agendamento, Funcionario funcionario) {
        FuncionarioAgendamento vinculo = new FuncionarioAgendamento();
        vinculo.setId(new FuncionarioAgendamentoId(funcionario.getId(), agendamento.getId()));
        vinculo.setFuncionario(funcionario);
        vinculo.setAgendamento(agendamento);
        agendamento.getFuncionarioAgendamentos().add(vinculo);
    }

    private void vincularServico(Agendamento agendamento, Servico servico) {
        AtendimentoServico atendimentoServico = new AtendimentoServico();
        atendimentoServico.setAgendamento(agendamento);
        atendimentoServico.setServico(servico);
        atendimentoServico.setValorUnitario(servico.getValor());
        atendimentoServico.setDescricao(servico.getDescricao());
        agendamento.getAtendimentoServicos().add(atendimentoServico);
    }

    private Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente nao encontrado"));
    }

    private Funcionario buscarFuncionario(Long funcionarioId) {
        return funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionario nao encontrado"));
    }

    private Sala buscarSala(Long salaId) {
        return salaRepository.findById(salaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala nao encontrada"));
    }

    private Servico buscarServico(Long servicoId) {
        return servicoRepository.findById(servicoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Servico nao encontrado"));
    }

    private Status buscarStatus(Long statusId) {
        return statusRepository.findById(statusId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status nao encontrado"));
    }
}
