package sptech.school.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.backend.config.GerenciadorTokenJwt;
import sptech.school.backend.dto.EnderecoDto.EnderecoDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioCriacaoDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioLoginDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioTokenDto;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.entity.Endereco;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.mapper.UsuarioMapper;
import sptech.school.backend.repository.ClienteRepository;
import sptech.school.backend.repository.EnderecoRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;

    public UsuarioService(
            PasswordEncoder passwordEncoder,
            UsuarioRepository usuarioRepository,
            EnderecoRepository enderecoRepository,
            ClienteRepository clienteRepository,
            FuncionarioRepository funcionarioRepository,
            GerenciadorTokenJwt gerenciadorTokenJwt,
            AuthenticationManager authenticationManager
    ) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void criar(UsuarioCriacaoDto dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }

        Endereco endereco = enderecoRepository.save(toEndereco(dto.getEndereco()));

        Usuario usuario = UsuarioMapper.toEntity(dto);
        usuario.setEndereco(endereco);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuarioSalvo);
        clienteRepository.save(cliente);
    }

    public UsuarioTokenDto login(UsuarioLoginDto dto) {
        UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha());

        Authentication authentication = authenticationManager.authenticate(credentials);

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = gerenciadorTokenJwt.generateToken(authentication);
        UsuarioTokenDto tokenDto = UsuarioMapper.toTokenDto(usuario, token);

        preencherPerfil(tokenDto, usuario);

        return tokenDto;
    }

    private void preencherPerfil(UsuarioTokenDto tokenDto, Usuario usuario) {
        funcionarioRepository.findByUsuarioId(usuario.getId()).ifPresentOrElse(
                funcionario -> preencherFuncionario(tokenDto, funcionario),
                () -> clienteRepository.findByUsuarioId(usuario.getId())
                        .ifPresent(cliente -> preencherCliente(tokenDto, cliente))
        );
    }

    private void preencherFuncionario(UsuarioTokenDto tokenDto, Funcionario funcionario) {
        tokenDto.setTipo("FUNCIONARIO");
        tokenDto.setFuncionarioId(funcionario.getId());

        Cargo cargo = funcionario.getCargo();
        tokenDto.setCargo(new UsuarioTokenDto.CargoResumoDto(cargo.getId(), cargo.getNome()));
        tokenDto.setPermissoes(
                cargo.getPermissoes()
                        .stream()
                        .map(permissao -> new UsuarioTokenDto.PermissaoResumoDto(
                                permissao.getId(),
                                permissao.getNome()
                        ))
                        .toList()
        );
    }

    private void preencherCliente(UsuarioTokenDto tokenDto, Cliente cliente) {
        tokenDto.setTipo("CLIENTE");
        tokenDto.setClienteId(cliente.getId());
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
