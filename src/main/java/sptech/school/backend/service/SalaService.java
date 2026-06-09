package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import sptech.school.backend.dto.SalaDto.SalaCriacaoDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.SalaRepository;

import java.util.List;

@Service
    public class SalaService {

        private final SalaRepository salaRepository;

    public SalaService(SalaRepository salaRepository){
        this.salaRepository = salaRepository;
    }

    public List<Sala> listar(){
        return salaRepository.findAll();
    }

    public Sala buscarPorId(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Sala nao encontrada"));
    }

    public Sala criar(SalaCriacaoDto dto) {
        Sala sala = new Sala();
        sala.setDescricao(dto.getDescricao());

        return salaRepository.save(sala);
    }

    public Sala atualizar(Long id, SalaCriacaoDto dto) {
        Sala sala = buscarPorId(id);

        sala.setDescricao(dto.getDescricao());

        return salaRepository.save(sala);
    }

    public void deletar(Long id) {
        Sala sala = buscarPorId(id);

        salaRepository.delete(sala);
    }
}
