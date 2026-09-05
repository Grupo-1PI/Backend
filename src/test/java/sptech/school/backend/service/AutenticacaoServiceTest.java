package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.UsuarioRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - AutenticacaoService")
class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private AutenticacaoService service;

    @DisplayName("Unidade: AutenticacaoService | Cenario: load user by username | Dados: quando usuario existe | Verifica: deve retornar user details")
    @Test
    void loadUserByUsername_deveRetornarUserDetails_quandoUsuarioExiste() {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@email.com");
        usuario.setSenha("hash");
        usuario.setAtivo(true);
        usuario.setId(1L);
        Mockito.when(usuarioRepository.findByEmail("usuario@email.com")).thenReturn(Optional.of(usuario));
        Mockito.when(funcionarioRepository.findByUsuarioId(1L)).thenReturn(Optional.empty());

        UserDetails resultado = service.loadUserByUsername("usuario@email.com");

        Assertions.assertEquals("usuario@email.com", resultado.getUsername());
        Assertions.assertEquals("hash", resultado.getPassword());
        Assertions.assertTrue(resultado.isEnabled());
    }

    @DisplayName("Unidade: AutenticacaoService | Cenario: load user by username | Dados: quando usuario nao existe | Verifica: deve lancar")
    @Test
    void loadUserByUsername_deveLancar_quandoUsuarioNaoExiste() {
        Mockito.when(usuarioRepository.findByEmail("usuario@email.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("usuario@email.com")
        );
    }
}
