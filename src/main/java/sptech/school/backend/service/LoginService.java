package sptech.school.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.backend.config.GerenciadorTokenJwt;
import sptech.school.backend.dto.UsuarioDto.UsuarioLoginDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioTokenDto;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.mapper.UsuarioMapper;
import sptech.school.backend.repository.ClienteRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.UsuarioRepository;

@Service
public class LoginService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(LoginService.class);

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ClienteRepository clienteRepository;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;
    private final LimiteTentativasLogin limiteTentativasLogin;

    public LoginService(
            UsuarioRepository usuarioRepository,
            FuncionarioRepository funcionarioRepository,
            ClienteRepository clienteRepository,
            GerenciadorTokenJwt gerenciadorTokenJwt,
            AuthenticationManager authenticationManager,
            LimiteTentativasLogin limiteTentativasLogin
    ) {
        this.usuarioRepository = usuarioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.clienteRepository = clienteRepository;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
        this.limiteTentativasLogin = limiteTentativasLogin;
    }

    public UsuarioTokenDto login(UsuarioLoginDto dto) {
        limiteTentativasLogin.verificar(dto.getEmail());

        UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha());

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(credentials);

            LOGGER.info(
                    "[AUTENTICACAO] Login realizado com sucesso - usuario={}",
                    dto.getEmail()
            );

        } catch (AuthenticationException e) {

            limiteTentativasLogin.registrarFalha(dto.getEmail());

            LOGGER.warn(
                    "[AUTENTICACAO] Falha de autenticacao - usuario={}",
                    dto.getEmail()
            );

            throw e;
        }

        limiteTentativasLogin.limpar(dto.getEmail());

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
}
