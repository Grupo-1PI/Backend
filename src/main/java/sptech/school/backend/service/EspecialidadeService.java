package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.backend.dto.EspecialidadeDto.EspecialidadeCriacaoDto;
import sptech.school.backend.entity.Especialidade;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.EspecialidadeRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.ServicoRepository;
import java.util.List;
import java.util.Objects;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;
    private final ServicoRepository servicoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public EspecialidadeService(
            EspecialidadeRepository especialidadeRepository,
            ServicoRepository servicoRepository,
            FuncionarioRepository funcionarioRepository
    ) {
        this.especialidadeRepository = especialidadeRepository;
        this.servicoRepository = servicoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Especialidade> listar() {
        return especialidadeRepository.findAll();
    }

    public Especialidade buscarPorId(Long id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade nao encontrada"));
    }

    @Transactional
    public Especialidade criar(EspecialidadeCriacaoDto dto) {
        Especialidade especialidade = new Especialidade();
        aplicarDados(especialidade, dto);

        return especialidadeRepository.save(especialidade);
    }

    @Transactional
    public Especialidade atualizar(Long id, EspecialidadeCriacaoDto dto) {
        Especialidade especialidade = buscarPorId(id);
        aplicarDados(especialidade, dto);

        return especialidadeRepository.save(especialidade);
    }

    @Transactional
    public void deletar(Long id) {
        Especialidade especialidade = buscarPorId(id);
        desvincularFuncionarios(id);
        especialidade.getServicos().clear();

        especialidadeRepository.delete(especialidade);
    }

    private void aplicarDados(Especialidade especialidade, EspecialidadeCriacaoDto dto) {
        especialidade.setNome(dto.getNome());
        especialidade.setServicos(buscarServicos(dto.getServicosIds()));
    }

    private List<Servico> buscarServicos(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        List<Long> idsDistintos = ids.stream().distinct().toList();
        List<Servico> servicos = servicoRepository.findAllById(idsDistintos);

        if (servicos.size() != idsDistintos.size()) {
            throw new RecursoNaoEncontradoException("Servico nao encontrado");
        }

        return servicos;
    }

    private void desvincularFuncionarios(Long especialidadeId) {
        List<Funcionario> funcionarios = funcionarioRepository.findByEspecialidadesId(especialidadeId);

        for (Funcionario funcionario : funcionarios) {
            funcionario.getEspecialidades()
                    .removeIf(especialidade -> Objects.equals(especialidade.getId(), especialidadeId));
        }
    }
}
