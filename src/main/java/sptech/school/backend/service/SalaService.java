package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.backend.dto.SalaDto.SalaCriacaoDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.exception.ConflitoException;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.AgendamentoRepository;
import sptech.school.backend.repository.SalaRepository;
import sptech.school.backend.repository.ServicoRepository;

import java.util.List;

@Service
    public class SalaService {

        private final SalaRepository salaRepository;
        private final AgendamentoRepository agendamentoRepository;
        private final ServicoRepository servicoRepository;

    public SalaService(
            SalaRepository salaRepository,
            AgendamentoRepository agendamentoRepository,
            ServicoRepository servicoRepository
    ){
        this.salaRepository = salaRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.servicoRepository = servicoRepository;
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

    @Transactional
    public void deletar(Long id) {
        Sala sala = buscarPorId(id);

        long agendamentos = agendamentoRepository.countBySalaId(id);
        long servicos = servicoRepository.countBySalasId(id);

        if (agendamentos > 0 || servicos > 0) {
            List<String> motivos = new java.util.ArrayList<>();
            if (agendamentos > 0) {
                motivos.add(agendamentos + (agendamentos == 1 ? " agendamento" : " agendamentos"));
            }
            if (servicos > 0) {
                motivos.add(servicos + (servicos == 1 ? " serviço vinculado" : " serviços vinculados"));
            }

            throw new ConflitoException(
                    "Não é possível excluir a sala \"" + sala.getDescricao() + "\" porque ela possui "
                            + String.join(" e ", motivos)
                            + ". Cancele os agendamentos e remova os vínculos de serviços antes de excluir."
            );
        }

        salaRepository.delete(sala);
    }
}
