package sptech.school.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.backend.dto.EnderecoDto.EnderecoDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioCriacaoDto;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.entity.Endereco;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.mapper.UsuarioMapper;
import sptech.school.backend.repository.ClienteRepository;
import sptech.school.backend.repository.EnderecoRepository;
import sptech.school.backend.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

@Service
public class UsuarioService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(UsuarioService.class);
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final ClienteRepository clienteRepository;

    public UsuarioService(
            PasswordEncoder passwordEncoder,
            UsuarioRepository usuarioRepository,
            EnderecoRepository enderecoRepository,
            ClienteRepository clienteRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;
        this.clienteRepository = clienteRepository;
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
