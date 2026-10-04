package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.backend.dto.ServicoDto.ServicoCriacaoDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.ConflitoException;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.AtendimentoServicoRepository;
import sptech.school.backend.repository.EspecialidadeRepository;
import sptech.school.backend.repository.SalaRepository;
import sptech.school.backend.repository.ServicoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final SalaRepository salaRepository;
    private final AtendimentoServicoRepository atendimentoServicoRepository;
    private final EspecialidadeRepository especialidadeRepository;

    public ServicoService(
            ServicoRepository servicoRepository,
            SalaRepository salaRepository,
            AtendimentoServicoRepository atendimentoServicoRepository,
            EspecialidadeRepository especialidadeRepository
    ) {
        this.servicoRepository = servicoRepository;
        this.salaRepository = salaRepository;
        this.atendimentoServicoRepository = atendimentoServicoRepository;
        this.especialidadeRepository = especialidadeRepository;
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
        Servico servico = buscarPorId(id);

        long atendimentos = atendimentoServicoRepository.countByServicoId(id);
        long especialidades = especialidadeRepository.countByServicosId(id);
        long salas = servicoRepository.countBySalasId(id);

        if (atendimentos > 0 || especialidades > 0 || salas > 0) {
            List<String> motivos = new ArrayList<>();
            if (atendimentos > 0) {
                motivos.add(atendimentos + (atendimentos == 1 ? " atendimento realizado" : " atendimentos realizados"));
            }
            if (especialidades > 0) {
                motivos.add(especialidades + (especialidades == 1 ? " especialidade" : " especialidades"));
            }
            if (salas > 0) {
                motivos.add(salas + (salas == 1 ? " sala vinculada" : " salas vinculadas"));
            }

            throw new ConflitoException(
                    "Não é possível excluir o serviço \"" + servico.getNome() + "\" porque ele está vinculado a "
                            + String.join(", ", motivos)
                            + ". Remova os vínculos antes de excluir."
            );
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
