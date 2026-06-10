package sptech.school.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sptech.school.backend.exception.GlobalExceptionHandler;
import sptech.school.backend.dto.EnderecoDto.EnderecoDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioCriacaoDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioLoginDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioTokenDto;
import sptech.school.backend.service.UsuarioService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - UsuarioController")
class UsuarioControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private UsuarioService usuarioService;

    @DisplayName("Unidade: UsuarioController | Cenario: post usuarios | Dados: dados preparados no arrange do teste | Verifica: deve retornar 201")
    @Test
    void postUsuarios_deveRetornar201() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criacaoDto())))
                .andExpect(status().isCreated());
    }

    @DisplayName("Unidade: UsuarioController | Cenario: post login | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200 com token")
    @Test
    void postLogin_deveRetornar200ComToken() throws Exception {
        Mockito.when(usuarioService.login(Mockito.any(UsuarioLoginDto.class))).thenReturn(tokenDto());

        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(cookie().value("authToken", "token"));
    }

    @DisplayName("Unidade: UsuarioController | Cenario: post logout | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void postLogout_deveRetornar200() throws Exception {
        mockMvc.perform(post("/usuarios/logout"))
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("authToken", 0));
    }

    private UsuarioCriacaoDto criacaoDto() {
        UsuarioCriacaoDto dto = new UsuarioCriacaoDto();
        dto.setNome("Cliente Teste");
        dto.setTelefone("11999990000");
        dto.setEmail("cliente@email.com");
        dto.setSenha("123456");
        dto.setDataNascimento(LocalDate.of(1990, 1, 1));
        dto.setEndereco(enderecoDto());
        return dto;
    }

    private UsuarioLoginDto loginDto() {
        UsuarioLoginDto dto = new UsuarioLoginDto();
        dto.setEmail("cliente@email.com");
        dto.setSenha("123456");
        return dto;
    }

    private UsuarioTokenDto tokenDto() {
        UsuarioTokenDto dto = new UsuarioTokenDto();
        dto.setUsuarioId(1L);
        dto.setNome("Cliente Teste");
        dto.setEmail("cliente@email.com");
        dto.setToken("token");
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
        return dto;
    }
}
