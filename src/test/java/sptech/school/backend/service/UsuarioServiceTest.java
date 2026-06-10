package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import sptech.school.backend.entity.Permissao;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.repository.ClienteRepository;
import sptech.school.backend.repository.EnderecoRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - UsuarioService")
class UsuarioServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UsuarioService service;

    @DisplayName("Unidade: UsuarioService | Cenario: criar | Dados: quando email nao existe | Verifica: deve criar usuario cliente")
    @Test
    void criar_deveCriarUsuarioCliente_quandoEmailNaoExiste() {
        UsuarioCriacaoDto dto = criacaoDto();
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(enderecoRepository.save(Mockito.any(Endereco.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(passwordEncoder.encode("123456")).thenReturn("hash");
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L);
            return usuario;
        });

        service.criar(dto);

        Mockito.verify(clienteRepository).save(Mockito.argThat(cliente -> cliente.getUsuario().getId().equals(1L)));
    }

    @DisplayName("Unidade: UsuarioService | Cenario: criar | Dados: quando email ja existe | Verifica: deve lancar")
    @Test
    void criar_deveLancar_quandoEmailJaExiste() {
        UsuarioCriacaoDto dto = criacaoDto();
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new Usuario()));

        Assertions.assertThrows(ResponseStatusException.class, () -> service.criar(dto));
    }

    @DisplayName("Unidade: UsuarioService | Cenario: login | Dados: quando usuario funcionario | Verifica: deve retornar token funcionario")
    @Test
    void login_deveRetornarTokenFuncionario_quandoUsuarioFuncionario() {
        UsuarioLoginDto dto = loginDto();
        Usuario usuario = usuario(1L);
        Funcionario funcionario = funcionario(2L);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(dto.getEmail(), null));
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));
        Mockito.when(gerenciadorTokenJwt.generateToken(Mockito.any())).thenReturn("token");
        Mockito.when(funcionarioRepository.findByUsuarioId(1L)).thenReturn(Optional.of(funcionario));

        UsuarioTokenDto resultado = service.login(dto);

        Assertions.assertEquals("token", resultado.getToken());
        Assertions.assertEquals("FUNCIONARIO", resultado.getTipo());
        Assertions.assertEquals(2L, resultado.getFuncionarioId());
        Assertions.assertEquals("Administrador", resultado.getCargo().getNome());
        Assertions.assertEquals(1, resultado.getPermissoes().size());
    }

    @DisplayName("Unidade: UsuarioService | Cenario: login | Dados: quando usuario cliente | Verifica: deve retornar token cliente")
    @Test
    void login_deveRetornarTokenCliente_quandoUsuarioCliente() {
        UsuarioLoginDto dto = loginDto();
        Usuario usuario = usuario(1L);
        Cliente cliente = new Cliente();
        cliente.setId(3L);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(dto.getEmail(), null));
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));
        Mockito.when(gerenciadorTokenJwt.generateToken(Mockito.any())).thenReturn("token");
        Mockito.when(funcionarioRepository.findByUsuarioId(1L)).thenReturn(Optional.empty());
        Mockito.when(clienteRepository.findByUsuarioId(1L)).thenReturn(Optional.of(cliente));

        UsuarioTokenDto resultado = service.login(dto);

        Assertions.assertEquals("CLIENTE", resultado.getTipo());
        Assertions.assertEquals(3L, resultado.getClienteId());
    }

    @DisplayName("Unidade: UsuarioService | Cenario: login | Dados: quando nao ha funcionario nem cliente | Verifica: deve retornar token sem perfil")
    @Test
    void login_deveRetornarTokenSemPerfil_quandoNaoHaFuncionarioNemCliente() {
        UsuarioLoginDto dto = loginDto();
        Usuario usuario = usuario(1L);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(dto.getEmail(), null));
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));
        Mockito.when(gerenciadorTokenJwt.generateToken(Mockito.any())).thenReturn("token");
        Mockito.when(funcionarioRepository.findByUsuarioId(1L)).thenReturn(Optional.empty());
        Mockito.when(clienteRepository.findByUsuarioId(1L)).thenReturn(Optional.empty());

        UsuarioTokenDto resultado = service.login(dto);

        Assertions.assertNull(resultado.getTipo());
        Assertions.assertEquals("token", resultado.getToken());
    }

    @DisplayName("Unidade: UsuarioService | Cenario: login | Dados: quando usuario nao encontrado apos autenticacao | Verifica: deve lancar")
    @Test
    void login_deveLancar_quandoUsuarioNaoEncontradoAposAutenticacao() {
        UsuarioLoginDto dto = loginDto();
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(dto.getEmail(), null));
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> service.login(dto));
    }

    private UsuarioCriacaoDto criacaoDto() {
        UsuarioCriacaoDto dto = new UsuarioCriacaoDto();
        dto.setNome("Usuario");
        dto.setTelefone("11999990000");
        dto.setEmail("usuario@email.com");
        dto.setSenha("123456");
        dto.setDataNascimento(LocalDate.of(1990, 1, 1));
        dto.setEndereco(enderecoDto());
        return dto;
    }

    private UsuarioLoginDto loginDto() {
        UsuarioLoginDto dto = new UsuarioLoginDto();
        dto.setEmail("usuario@email.com");
        dto.setSenha("123456");
        return dto;
    }

    private EnderecoDto enderecoDto() {
        EnderecoDto dto = new EnderecoDto();
        dto.setCep("01001-000");
        dto.setLogradouro("Rua A");
        dto.setBairro("Centro");
        dto.setCidade("Sao Paulo");
        dto.setUf("SP");
        dto.setNumero("100");
        dto.setComplemento("Apto 1");
        return dto;
    }

    private Usuario usuario(Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Usuario");
        usuario.setEmail("usuario@email.com");
        return usuario;
    }

    private Funcionario funcionario(Long id) {
        Permissao permissao = new Permissao();
        permissao.setId(1L);
        permissao.setNome("CRUD_USUARIO");

        Cargo cargo = new Cargo();
        cargo.setId(1L);
        cargo.setNome("Administrador");
        cargo.setPermissoes(List.of(permissao));

        Funcionario funcionario = new Funcionario();
        funcionario.setId(id);
        funcionario.setCargo(cargo);
        return funcionario;
    }
}
