package sptech.school.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.backend.config.GerenciadorTokenJwt;
import sptech.school.backend.dto.UsuarioListarDto;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.dto.UsuarioLoginDto;
import sptech.school.backend.dto.UsuarioTokenDto;
import sptech.school.backend.dto.UsuarioCriacaoDto;
import sptech.school.backend.mapper.UsuarioMapper;
import sptech.school.backend.repository.ClienteRepository;

@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void criar(Cliente novoUsuario) {


        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        this.clienteRepository.save(novoUsuario);
    }

    public UsuarioTokenDto autenticar(Cliente usuario) {

        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(), usuario.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Cliente usuarioAutenticado =
                clienteRepository.findByEmail(usuario.getEmail())
                        .orElseThrow(
                                () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, token);
    }

    public List<UsuarioListarDto> listarTodos() {

        List<Cliente> usuariosEncontrados = clienteRepository.findAll();
        return usuariosEncontrados.stream().map(UsuarioMapper::of).toList();

    }
}