package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.repository.AgendamentoRepository;
import sptech.school.backend.repository.ClienteRepository;
import sptech.school.backend.strategy.RegraAgendamentoStrategy;

import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final ClienteRepository clienteRepository;
    private final List<RegraAgendamentoStrategy> regras;

    public AgendamentoService(AgendamentoRepository repository,
                              ClienteRepository clienteRepository,
                              List<RegraAgendamentoStrategy> regras) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.regras = regras;
    }

    public Agendamento criar(Agendamento agendamento, Long clienteId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        agendamento.setCliente(cliente);

        for (RegraAgendamentoStrategy regra : regras) {
            regra.validar(agendamento);
        }

        return repository.save(agendamento);
    }

    public List<Agendamento> listar() {
        return repository.findAll();
    }

    public Agendamento atualizar(Long id, Agendamento novo, Long clienteId) {

        Agendamento existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        existente.setDataHora(novo.getDataHora());
        existente.setCliente(cliente);

        for (RegraAgendamentoStrategy regra : regras) {
            regra.validar(existente);
        }

        return repository.save(existente);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}