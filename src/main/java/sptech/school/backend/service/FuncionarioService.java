package sptech.school.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.UsuarioRepository;
import sptech.school.backend.repository.CargoRepository;
import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final CargoRepository cargoRepository;

    public FuncionarioService(
            FuncionarioRepository funcionarioRepository,
            UsuarioRepository usuarioRepository,
            CargoRepository cargoRepository
    ) {
        this.funcionarioRepository = funcionarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.cargoRepository = cargoRepository;
    }

    public Funcionario criar(Long usuarioId, Long cargoId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Cargo cargo = cargoRepository.findById(cargoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cargo não encontrado"));

        Funcionario f = new Funcionario();
        f.setUsuario(usuario);
        f.setCargo(cargo);

        return funcionarioRepository.save(f);
    }

    public List<Funcionario> listar() {
        return funcionarioRepository.findAll();
    }

    public Funcionario atualizar(Long id, Long cargoId) {

        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        Cargo cargo = cargoRepository.findById(cargoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cargo não encontrado"));

        f.setCargo(cargo);

        return funcionarioRepository.save(f);
    }

    public void deletar(Long id) {
        funcionarioRepository.deleteById(id);
    }
}