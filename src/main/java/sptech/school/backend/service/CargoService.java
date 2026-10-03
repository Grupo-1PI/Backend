package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.backend.dto.CargoDto.CargoCriacaoDto;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.entity.Permissao;
import sptech.school.backend.exception.ConflitoException;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.CargoRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.PermissaoRepository;
import java.util.ArrayList;
import java.util.List;

@Service
public class CargoService {

    private final CargoRepository cargoRepository;
    private final PermissaoRepository permissaoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public CargoService(
            CargoRepository cargoRepository,
            PermissaoRepository permissaoRepository,
            FuncionarioRepository funcionarioRepository
    ) {
        this.cargoRepository = cargoRepository;
        this.permissaoRepository = permissaoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Cargo> listar() {
        return cargoRepository.findAll();
    }

    public Cargo buscarPorId(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cargo não encontrado"));
    }

    @Transactional
    public Cargo criar(CargoCriacaoDto dto) {
        Cargo cargo = new Cargo();
        aplicarDados(cargo, dto);

        return cargoRepository.save(cargo);
    }

    @Transactional
    public Cargo atualizar(Long id, CargoCriacaoDto dto) {
        Cargo cargo = buscarPorId(id);
        aplicarDados(cargo, dto);

        return cargoRepository.save(cargo);
    }

    @Transactional
    public void deletar(Long id) {
        Cargo cargo = buscarPorId(id);

        long funcionarios = funcionarioRepository.countByCargoId(id);
        long permissoes = cargo.getPermissoes() != null ? cargo.getPermissoes().size() : 0;

        if (funcionarios > 0 || permissoes > 0) {
            List<String> motivos = new ArrayList<>();
            if (funcionarios > 0) {
                motivos.add(funcionarios + (funcionarios == 1 ? " funcionário vinculado" : " funcionários vinculados"));
            }
            if (permissoes > 0) {
                motivos.add(permissoes + (permissoes == 1 ? " permissão associada" : " permissões associadas"));
            }

            throw new ConflitoException(
                    "Não é possível excluir o cargo \"" + cargo.getNome() + "\" porque ele possui "
                            + String.join(" e ", motivos)
                            + ". Remova os vínculos antes de excluir."
            );
        }

        cargoRepository.deleteById(id);
    }

    private void aplicarDados(Cargo cargo, CargoCriacaoDto dto) {
        cargo.setNome(dto.getNome());
        cargo.setDescricao(dto.getDescricao());
        cargo.setPermissoes(buscarPermissoes(dto.getPermissoesIds()));
    }

    private List<Permissao> buscarPermissoes(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        List<Long> idsDistintos = ids.stream().distinct().toList();
        List<Permissao> permissoes = permissaoRepository.findAllById(idsDistintos);

        if (permissoes.size() != idsDistintos.size()) {
            throw new RecursoNaoEncontradoException("Permissão não encontrada");
        }

        return permissoes;
    }
}
