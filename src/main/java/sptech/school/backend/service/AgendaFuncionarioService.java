package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.backend.dto.AgendaDto.AgendaExcecaoDto;
import sptech.school.backend.dto.AgendaDto.AgendaFuncionarioDto;
import sptech.school.backend.dto.AgendaDto.AgendaFuncionarioListagemDto;
import sptech.school.backend.entity.AgendaExcecao;
import sptech.school.backend.entity.AgendaFuncionario;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.AgendaExcecaoRepository;
import sptech.school.backend.repository.AgendaFuncionarioRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AgendaFuncionarioService {

    private final AgendaFuncionarioRepository agendaFuncionarioRepository;
    private final AgendaExcecaoRepository agendaExcecaoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public AgendaFuncionarioService(
            AgendaFuncionarioRepository agendaFuncionarioRepository,
            AgendaExcecaoRepository agendaExcecaoRepository,
            FuncionarioRepository funcionarioRepository
    ) {
        this.agendaFuncionarioRepository = agendaFuncionarioRepository;
        this.agendaExcecaoRepository = agendaExcecaoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<AgendaFuncionario> listarPorFuncionario(Long funcionarioId) {
        return agendaFuncionarioRepository.findByFuncionarioId(funcionarioId);
    }

    @Transactional(readOnly = true)
    public List<AgendaFuncionarioListagemDto> listarTodosComAgenda() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return funcionarioRepository.findAll().stream()
                .map(funcionario -> {
                    List<AgendaFuncionarioListagemDto.AgendaItemDto> agendas = agendaFuncionarioRepository
                            .findByFuncionarioId(funcionario.getId())
                            .stream()
                            .map(agenda -> new AgendaFuncionarioListagemDto.AgendaItemDto(
                                    agenda.getId(),
                                    agenda.getDiaSemana(),
                                    agenda.getHoraInicio().format(formatter),
                                    agenda.getHoraFim().format(formatter)
                            ))
                            .toList();

                    String especialidadePrincipal = funcionario.getEspecialidades().isEmpty()
                            ? null
                            : funcionario.getEspecialidades().get(0).getNome();

                    return new AgendaFuncionarioListagemDto(
                            funcionario.getId(),
                            funcionario.getUsuario().getNome(),
                            especialidadePrincipal,
                            agendas
                    );
                })
                .toList();
    }

    @Transactional
    public AgendaFuncionario criar(AgendaFuncionarioDto dto) {
        AgendaFuncionario agenda = new AgendaFuncionario();
        aplicarDados(agenda, dto);
        return agendaFuncionarioRepository.save(agenda);
    }

    @Transactional
    public AgendaFuncionario atualizar(Long id, AgendaFuncionarioDto dto) {
        AgendaFuncionario agenda = agendaFuncionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agenda nao encontrada"));

        aplicarDados(agenda, dto);
        return agendaFuncionarioRepository.save(agenda);
    }

    @Transactional
    public void deletar(Long id) {
        if (!agendaFuncionarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Agenda nao encontrada");
        }

        agendaFuncionarioRepository.deleteById(id);
    }

    @Transactional
    public AgendaExcecao criarExcecao(AgendaExcecaoDto dto) {
        AgendaExcecao excecao = new AgendaExcecao();
        aplicarDadosExcecao(excecao, dto);
        return agendaExcecaoRepository.save(excecao);
    }

    @Transactional
    public void deletarExcecao(Long id) {
        if (!agendaExcecaoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Excecao de agenda nao encontrada");
        }

        agendaExcecaoRepository.deleteById(id);
    }

    public List<AgendaExcecao> listarExcecoesPorFuncionario(Long funcionarioId) {
        return agendaExcecaoRepository.findByFuncionarioId(funcionarioId);
    }

    private void aplicarDados(AgendaFuncionario agenda, AgendaFuncionarioDto dto) {
        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionario nao encontrado"));

        agenda.setFuncionario(funcionario);
        agenda.setDiaSemana(dto.getDiaSemana());
        agenda.setHoraInicio(dto.getHoraInicio());
        agenda.setHoraFim(dto.getHoraFim());
    }

    private void aplicarDadosExcecao(AgendaExcecao excecao, AgendaExcecaoDto dto) {
        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionario nao encontrado"));

        excecao.setFuncionario(funcionario);
        excecao.setData(dto.getData());
        excecao.setHoraInicio(dto.getHoraInicio());
        excecao.setHoraFim(dto.getHoraFim());
        excecao.setDisponivel(dto.getDisponivel());
    }
}
