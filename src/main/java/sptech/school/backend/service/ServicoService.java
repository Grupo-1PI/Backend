package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import sptech.school.backend.dto.ServicoDto.ServicoRequestDto;
import sptech.school.backend.dto.ServicoDto.ServicoResponseDto;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.mapper.ServicoMapper;
import sptech.school.backend.repository.ServicoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {
    private final ServicoRepository repository;
    private final ServicoMapper mapper;

    public ServicoService(ServicoRepository repository, ServicoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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

    public void atualizar(Long id) {
        Optional<Servico> servico = repository.findById(id);
        if (servico.isEmpty()) {
            throw new RecursoNaoEncontradoException("Serviço não encontrado");
        }

    }
}
