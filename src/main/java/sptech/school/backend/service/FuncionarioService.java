package sptech.school.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.backend.dto.EnderecoDto.EnderecoDto;
import sptech.school.backend.dto.FuncionarioDto.FuncionarioAtualizacaoDto;
import sptech.school.backend.dto.FuncionarioDto.FuncionarioCriacaoDto;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.entity.Endereco;
import sptech.school.backend.entity.Especialidade;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.CargoRepository;
import sptech.school.backend.repository.EnderecoRepository;
import sptech.school.backend.repository.EspecialidadeRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.UsuarioRepository;
import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final CargoRepository cargoRepository;
    private final EnderecoRepository enderecoRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(
            FuncionarioRepository funcionarioRepository,
            UsuarioRepository usuarioRepository,
            CargoRepository cargoRepository,
            EnderecoRepository enderecoRepository,
            EspecialidadeRepository especialidadeRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.funcionarioRepository = funcionarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.cargoRepository = cargoRepository;
        this.enderecoRepository = enderecoRepository;
        this.especialidadeRepository = especialidadeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Funcionario criar(FuncionarioCriacaoDto dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }

        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cargo nao encontrado"));

        Endereco endereco = enderecoRepository.save(toEndereco(dto.getEndereco()));

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setAtivo(true);
        usuario.setEndereco(endereco);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Funcionario funcionario = new Funcionario();
        funcionario.setUsuario(usuarioSalvo);
        funcionario.setCargo(cargo);
        funcionario.setEspecialidades(buscarEspecialidades(dto.getEspecialidadesIds()));

        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> listar() {
        return funcionarioRepository.findAll();
    }

    public Funcionario buscarPorId(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionario nao encontrado"));
    }

    @Transactional
    public Funcionario atualizar(Long id, FuncionarioAtualizacaoDto dto) {
        Funcionario funcionario = buscarPorId(id);

        if (dto.getCargoId() != null) {
            Cargo cargo = cargoRepository.findById(dto.getCargoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Cargo nao encontrado"));
            funcionario.setCargo(cargo);
        }

        if (dto.getEspecialidadesIds() != null) {
            funcionario.setEspecialidades(buscarEspecialidades(dto.getEspecialidadesIds()));
        }

        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public void deletar(Long id) {
        Funcionario funcionario = buscarPorId(id);
        funcionarioRepository.delete(funcionario);
    }

    private List<Especialidade> buscarEspecialidades(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        List<Long> idsDistintos = ids.stream().distinct().toList();
        List<Especialidade> especialidades = especialidadeRepository.findAllById(idsDistintos);

        if (especialidades.size() != idsDistintos.size()) {
            throw new RecursoNaoEncontradoException("Especialidade nao encontrada");
        }

        return especialidades;
    }

    private Endereco toEndereco(EnderecoDto dto) {
        Endereco endereco = new Endereco();
        endereco.setCep(dto.getCep());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setUf(dto.getUf());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        return endereco;
    }
}
