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
import sptech.school.backend.dto.SalaDto.SalaCriacaoDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.service.SalaService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - SalaController")
class SalaControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private SalaController salaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(salaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private SalaService salaService;

    @DisplayName("Unidade: SalaController | Cenario: get salas | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getSalas_deveRetornar200() throws Exception {
        Mockito.when(salaService.listar()).thenReturn(List.of(sala(1L, "Sala 1")));

        mockMvc.perform(get("/salas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Sala 1"));
    }

    @DisplayName("Unidade: SalaController | Cenario: get sala por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getSalaPorId_deveRetornar200() throws Exception {
        Mockito.when(salaService.buscarPorId(1L)).thenReturn(sala(1L, "Sala 1"));

        mockMvc.perform(get("/salas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Unidade: SalaController | Cenario: get sala por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 404")
    @Test
    void getSalaPorId_deveRetornar404() throws Exception {
        Mockito.when(salaService.buscarPorId(999L)).thenThrow(new RecursoNaoEncontradoException("Sala nao encontrada"));

        mockMvc.perform(get("/salas/999"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Unidade: SalaController | Cenario: post salas | Dados: dados preparados no arrange do teste | Verifica: deve retornar 201")
    @Test
    void postSalas_deveRetornar201() throws Exception {
        SalaCriacaoDto dto = new SalaCriacaoDto("Sala Nova");
        Mockito.when(salaService.criar(Mockito.any(SalaCriacaoDto.class))).thenReturn(sala(1L, "Sala Nova"));

        mockMvc.perform(post("/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Sala Nova"));
    }

    @DisplayName("Unidade: SalaController | Cenario: put salas | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void putSalas_deveRetornar200() throws Exception {
        SalaCriacaoDto dto = new SalaCriacaoDto("Sala Editada");
        Mockito.when(salaService.atualizar(Mockito.eq(1L), Mockito.any(SalaCriacaoDto.class)))
                .thenReturn(sala(1L, "Sala Editada"));

        mockMvc.perform(put("/salas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Sala Editada"));
    }

    @DisplayName("Unidade: SalaController | Cenario: delete salas | Dados: dados preparados no arrange do teste | Verifica: deve retornar 204")
    @Test
    void deleteSalas_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/salas/1"))
                .andExpect(status().isNoContent());
    }

    private Sala sala(Long id, String descricao) {
        Sala sala = new Sala();
        sala.setId(id);
        sala.setDescricao(descricao);
        return sala;
    }
}
