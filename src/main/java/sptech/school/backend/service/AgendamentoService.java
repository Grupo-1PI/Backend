package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import sptech.school.backend.entity.*;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.*;
import sptech.school.backend.strategy.RegraAgendamentoStrategy;
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

    public Agendamento criar(
            Agendamento agendamento,
            Long clienteId,
            Long funcionarioId,
            Long salaId,
            Long servicoId,
            Long statusId
    ) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));

        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado"));

        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala não encontrada"));

        Servico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));

        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status não encontrado"));

        agendamento.setCliente(cliente);
        agendamento.setFuncionario(funcionario);
        agendamento.setSala(sala);
        agendamento.setServico(servico);
        agendamento.setStatus(status);

        for (RegraAgendamentoStrategy regra : regras) {
            regra.validar(agendamento);
        }

        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listar() {
        return agendamentoRepository.findAll();
    }

    public Agendamento atualizar(
            Long id,
            Agendamento novo,
            Long clienteId,
            Long funcionarioId,
            Long salaId,
            Long servicoId,
            Long statusId
    ) {

        Agendamento existente = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));

        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado"));

        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala não encontrada"));

        Servico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));

        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status não encontrado"));

        existente.setDataHoraInicio(novo.getDataHoraInicio());
        existente.setDataHoraFim(novo.getDataHoraFim());
        existente.setObservacao(novo.getObservacao());
        existente.setCliente(cliente);
        existente.setFuncionario(funcionario);
        existente.setSala(sala);
        existente.setServico(servico);
        existente.setStatus(status);

        for (RegraAgendamentoStrategy regra : regras) {
            regra.validar(existente);
        }

        return agendamentoRepository.save(existente);
    }

    public void deletar(Long id) {

        if (!agendamentoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Agendamento não encontrado");
        }

        agendamentoRepository.deleteById(id);
    }
}