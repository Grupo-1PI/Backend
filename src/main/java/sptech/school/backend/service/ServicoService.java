package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.backend.dto.ServicoDto.ServicoCriacaoDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.SalaRepository;
import sptech.school.backend.repository.ServicoRepository;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final SalaRepository salaRepository;

    public ServicoService(ServicoRepository servicoRepository, SalaRepository salaRepository) {
        this.servicoRepository = servicoRepository;
        this.salaRepository = salaRepository;
    }

    public List<Servico> listar() {
        return servicoRepository.findAll();
    }

    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Servico nao encontrado"));
    }

    @Transactional
    public Servico criar(ServicoCriacaoDto dto) {
        Servico servico = new Servico();
        aplicarDados(servico, dto);
        return servicoRepository.save(servico);
    }

    @Transactional
    public Servico atualizar(Long id, ServicoCriacaoDto dto) {
        Servico servico = buscarPorId(id);
        aplicarDados(servico, dto);
        return servicoRepository.save(servico);
    }

    @Transactional
    public void deletar(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Servico nao encontrado");
        }

        servicoRepository.deleteById(id);
    }

    private void aplicarDados(Servico servico, ServicoCriacaoDto dto) {
        servico.setNome(dto.getNome());
        servico.setValor(dto.getValor());
        servico.setDescricao(dto.getDescricao());
        servico.setTempoMedio(dto.getTempoMedio());

        servico.getSalas().clear();
        servico.getSalas().addAll(buscarSalas(dto.getSalasIds()));
    }

    private List<Sala> buscarSalas(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        return salaRepository.findAllById(ids);
    }
}
