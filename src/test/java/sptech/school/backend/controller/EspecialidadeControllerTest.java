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
import sptech.school.backend.dto.EspecialidadeDto.EspecialidadeCriacaoDto;
import sptech.school.backend.entity.Especialidade;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.service.EspecialidadeService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - EspecialidadeController")
class EspecialidadeControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private EspecialidadeController especialidadeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(especialidadeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private EspecialidadeService especialidadeService;

    @DisplayName("Unidade: EspecialidadeController | Cenario: get especialidades | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getEspecialidades_deveRetornar200() throws Exception {
        Mockito.when(especialidadeService.listar()).thenReturn(List.of(especialidade(1L, "Acupuntura")));

        mockMvc.perform(get("/especialidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Acupuntura"));
    }

    @DisplayName("Unidade: EspecialidadeController | Cenario: get especialidade por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getEspecialidadePorId_deveRetornar200() throws Exception {
        Mockito.when(especialidadeService.buscarPorId(1L)).thenReturn(especialidade(1L, "Acupuntura"));

        mockMvc.perform(get("/especialidades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Unidade: EspecialidadeController | Cenario: get especialidade por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 404")
    @Test
    void getEspecialidadePorId_deveRetornar404() throws Exception {
        Mockito.when(especialidadeService.buscarPorId(999L))
                .thenThrow(new RecursoNaoEncontradoException("Especialidade nao encontrada"));

        mockMvc.perform(get("/especialidades/999"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Unidade: EspecialidadeController | Cenario: post especialidades | Dados: dados preparados no arrange do teste | Verifica: deve retornar 201")
    @Test
    void postEspecialidades_deveRetornar201() throws Exception {
        Mockito.when(especialidadeService.criar(Mockito.any(EspecialidadeCriacaoDto.class)))
                .thenReturn(especialidade(1L, "Nova"));

        mockMvc.perform(post("/especialidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Nova"));
    }

    @DisplayName("Unidade: EspecialidadeController | Cenario: put especialidades | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void putEspecialidades_deveRetornar200() throws Exception {
        Mockito.when(especialidadeService.atualizar(Mockito.eq(1L), Mockito.any(EspecialidadeCriacaoDto.class)))
                .thenReturn(especialidade(1L, "Editada"));

        mockMvc.perform(put("/especialidades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Editada"));
    }

    @DisplayName("Unidade: EspecialidadeController | Cenario: delete especialidades | Dados: dados preparados no arrange do teste | Verifica: deve retornar 204")
    @Test
    void deleteEspecialidades_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/especialidades/1"))
                .andExpect(status().isNoContent());
    }

    private EspecialidadeCriacaoDto dto() {
        EspecialidadeCriacaoDto dto = new EspecialidadeCriacaoDto();
        dto.setNome("Especialidade");
        dto.setServicosIds(List.of(1L));
        return dto;
    }

    private Especialidade especialidade(Long id, String nome) {
        Especialidade especialidade = new Especialidade();
        especialidade.setId(id);
        especialidade.setNome(nome);
        return especialidade;
    }
}
