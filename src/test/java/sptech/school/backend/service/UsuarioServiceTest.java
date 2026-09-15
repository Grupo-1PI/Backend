package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.backend.dto.EnderecoDto.EnderecoDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioCriacaoDto;
import sptech.school.backend.entity.Endereco;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.repository.ClienteRepository;
import sptech.school.backend.repository.EnderecoRepository;
import sptech.school.backend.repository.UsuarioRepository;

import java.time.LocalDate;
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
}
