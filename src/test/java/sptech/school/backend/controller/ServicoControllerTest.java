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
import sptech.school.backend.dto.ServicoDto.ServicoCriacaoDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.service.ServicoService;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - ServicoController")
class ServicoControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private ServicoController servicoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(servicoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private ServicoService servicoService;

    @DisplayName("Unidade: ServicoController | Cenario: get servicos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getServicos_deveRetornar200() throws Exception {
        Mockito.when(servicoService.listar()).thenReturn(List.of(servico(1L, "Servico")));

        mockMvc.perform(get("/servicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].salas[0]").value("Sala 1"));
    }

    @DisplayName("Unidade: ServicoController | Cenario: get servico por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getServicoPorId_deveRetornar200() throws Exception {
        Mockito.when(servicoService.buscarPorId(1L)).thenReturn(servico(1L, "Servico"));

        mockMvc.perform(get("/servicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Servico"));
    }

    @DisplayName("Unidade: ServicoController | Cenario: get servico por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 404")
    @Test
    void getServicoPorId_deveRetornar404() throws Exception {
        Mockito.when(servicoService.buscarPorId(999L)).thenThrow(new RecursoNaoEncontradoException("Servico nao encontrado"));

        mockMvc.perform(get("/servicos/999"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Unidade: ServicoController | Cenario: post servicos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 201")
    @Test
    void postServicos_deveRetornar201() throws Exception {
        Mockito.when(servicoService.criar(Mockito.any(ServicoCriacaoDto.class))).thenReturn(servico(1L, "Servico"));

        mockMvc.perform(post("/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Servico"));
    }

    @DisplayName("Unidade: ServicoController | Cenario: put servicos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void putServicos_deveRetornar200() throws Exception {
        Mockito.when(servicoService.atualizar(Mockito.eq(1L), Mockito.any(ServicoCriacaoDto.class)))
                .thenReturn(servico(1L, "Servico Editado"));

        mockMvc.perform(put("/servicos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Servico Editado"));
    }

    @DisplayName("Unidade: ServicoController | Cenario: delete servicos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 204")
    @Test
    void deleteServicos_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/servicos/1"))
                .andExpect(status().isNoContent());
    }

    private ServicoCriacaoDto dto() {
        ServicoCriacaoDto dto = new ServicoCriacaoDto();
        dto.setNome("Servico");
        dto.setValor(new BigDecimal("100.00"));
        dto.setDescricao("Descricao");
        dto.setTempoMedio(60);
        dto.setSalasIds(List.of(1L));
        return dto;
    }

    private Servico servico(Long id, String nome) {
        Sala sala = new Sala();
        sala.setId(1L);
        sala.setDescricao("Sala 1");

        Servico servico = new Servico();
        servico.setId(id);
        servico.setNome(nome);
        servico.setValor(new BigDecimal("100.00"));
        servico.setDescricao("Descricao");
        servico.setTempoMedio(60);
        servico.getSalas().add(sala);
        return servico;
    }
}
