package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import sptech.school.backend.dto.ServicoDto.ServicoRequestDto;
import sptech.school.backend.dto.ServicoDto.ServicoResponseDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.mapper.ServicoMapper;
import sptech.school.backend.repository.SalaRepository;
import sptech.school.backend.repository.ServicoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicoService {
    private final ServicoRepository repository;
    private final ServicoMapper mapper;
    private final SalaRepository salaRepository;

    public ServicoService(ServicoRepository repository, ServicoMapper mapper, SalaRepository salaRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.salaRepository = salaRepository;
    }

    public List<ServicoResponseDto> listar() {
        List<Servico> servicos = repository.findAll();
        if (servicos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Serviços não encontrados");
        }
        return mapper.toResponseList(servicos);
    }

    public void salvar(ServicoRequestDto requestDto) {
        Servico servico = mapper.toEntity(requestDto);
        repository.save(servico);
    }

    public void atualizarEntidade(Long id, ServicoRequestDto requestDto) {
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));

        servico.setNome(requestDto.getNome());
        servico.setValor(requestDto.getValor());
        servico.setDescricao(requestDto.getDescricao());
        servico.setTempoMedio(requestDto.getTempoMedio());
        repository.save(servico);
    }

    public void atualizar(Long id, ServicoRequestDto requestDto) {
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));

        if (requestDto.getNome() != null) {
            servico.setNome(requestDto.getNome());
        }
        if (requestDto.getValor() != null) {
            servico.setValor(requestDto.getValor());
        }
        if (requestDto.getDescricao() != null) {
            servico.setDescricao(requestDto.getDescricao());
        }
        if (requestDto.getTempoMedio() != null) {
            servico.setTempoMedio(requestDto.getTempoMedio());
        }
        if (requestDto.getSalasIds() != null && !requestDto.getSalasIds().isEmpty()) {
            List<Sala> salas = new ArrayList<>();
            requestDto.getSalasIds().forEach(salaId ->
                    salaRepository.findById(salaId).ifPresent(salas::add)
            );
            servico.setSalas(salas);
        }
        repository.save(servico);
    }
}
