package sptech.school.backend.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class GerenciadorTokenJwtTest {

    private static final String SECRET = "RXhpc3RlIHVtYSB0ZW9yaWEgcXVlIGRp" +
            "eiBxdWUsIHNlIHVtIGRpYSBhbGd1bWEgY2hhdmU=";

    private GerenciadorTokenJwt gerenciador;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        gerenciador = new GerenciadorTokenJwt();
        ReflectionTestUtils.setField(gerenciador, "secret", SECRET);
        ReflectionTestUtils.setField(gerenciador, "jwtTokenValidity", 3600L);
        lenient().when(authentication.getName()).thenReturn("usuario@teste.com");
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        lenient().doReturn(authorities).when(authentication).getAuthorities();
    }

    @Test
    void validateToken_deveRetornarTrue_quandoTokenValido() {
        String token = gerenciador.generateToken(authentication);

        assertTrue(gerenciador.validateToken(token));
    }

    @Test
    void validateToken_deveRetornarFalse_quandoTokenExpirado() {
        ReflectionTestUtils.setField(gerenciador, "jwtTokenValidity", -1L);
        String token = gerenciador.generateToken(authentication);

        assertFalse(gerenciador.validateToken(token));
    }

    @Test
    void validateToken_deveRetornarFalse_quandoTokenAdulterado() {
        String token = gerenciador.generateToken(authentication);
        String tokenAdulterado = token.substring(0, token.length() - 5) + "abcde";

        assertFalse(gerenciador.validateToken(tokenAdulterado));
    }

    @Test
    void validateToken_deveRetornarFalse_quandoTokenVazio() {
        assertFalse(gerenciador.validateToken(""));
    }

    @Test
    void validateToken_deveRetornarFalse_quandoTokenNull() {
        assertFalse(gerenciador.validateToken(null));
    }

    @Test
    void validateToken_naoDeveLancarExcecao_emNenhumCenario() {
        String tokenValido = gerenciador.generateToken(authentication);
        ReflectionTestUtils.setField(gerenciador, "jwtTokenValidity", -1L);
        String tokenExpirado = gerenciador.generateToken(authentication);
        String tokenAdulterado = tokenValido.substring(0, tokenValido.length() - 5) + "abcde";

        assertDoesNotThrow(() -> {
            gerenciador.validateToken(tokenValido);
            gerenciador.validateToken(tokenExpirado);
            gerenciador.validateToken(tokenAdulterado);
            gerenciador.validateToken("");
            gerenciador.validateToken(null);
        });
    }
}
