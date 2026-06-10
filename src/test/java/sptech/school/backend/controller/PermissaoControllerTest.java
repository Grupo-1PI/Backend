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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sptech.school.backend.exception.GlobalExceptionHandler;
import sptech.school.backend.entity.Permissao;
import sptech.school.backend.repository.PermissaoRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - PermissaoController")
class PermissaoControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private PermissaoController permissaoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(permissaoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private PermissaoRepository permissaoRepository;

    @DisplayName("Unidade: PermissaoController | Cenario: get permissoes | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getPermissoes_deveRetornar200() throws Exception {
        Mockito.when(permissaoRepository.findAll()).thenReturn(List.of(permissao(1L, "CRUD_USUARIO")));

        mockMvc.perform(get("/permissoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("CRUD_USUARIO"));
    }

    private Permissao permissao(Long id, String nome) {
        Permissao permissao = new Permissao();
        permissao.setId(id);
        permissao.setNome(nome);
        return permissao;
    }
}
