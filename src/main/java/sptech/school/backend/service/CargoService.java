package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.CargoRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.PermissaoRepository;
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
}
