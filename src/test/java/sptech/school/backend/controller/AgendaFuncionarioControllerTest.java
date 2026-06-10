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
import sptech.school.backend.dto.AgendaDto.AgendaExcecaoDto;
import sptech.school.backend.dto.AgendaDto.AgendaFuncionarioDto;
import sptech.school.backend.dto.AgendaDto.AgendaFuncionarioListagemDto;
import sptech.school.backend.entity.AgendaExcecao;
import sptech.school.backend.entity.AgendaFuncionario;
import sptech.school.backend.service.AgendaFuncionarioService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - AgendaFuncionarioController")
class AgendaFuncionarioControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private AgendaFuncionarioController agendaFuncionarioController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(agendaFuncionarioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private AgendaFuncionarioService agendaFuncionarioService;

    @DisplayName("Unidade: AgendaFuncionarioController | Cenario: get agenda funcionario | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getAgendaFuncionario_deveRetornar200() throws Exception {
        AgendaFuncionarioListagemDto dto = new AgendaFuncionarioListagemDto(1L, "Funcionario", null, List.of());
        Mockito.when(agendaFuncionarioService.listarTodosComAgenda()).thenReturn(List.of(dto));

        mockMvc.perform(get("/agenda-funcionario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].funcionarioNome").value("Funcionario"));
    }

    @DisplayName("Unidade: AgendaFuncionarioController | Cenario: get agenda funcionario por funcionario | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getAgendaFuncionarioPorFuncionario_deveRetornar200() throws Exception {
        Mockito.when(agendaFuncionarioService.listarPorFuncionario(1L)).thenReturn(List.of(agenda(1L)));

        mockMvc.perform(get("/agenda-funcionario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].diaSemana").value(2));
    }

    @DisplayName("Unidade: AgendaFuncionarioController | Cenario: post agenda funcionario | Dados: dados preparados no arrange do teste | Verifica: deve retornar 201")
    @Test
    void postAgendaFuncionario_deveRetornar201() throws Exception {
        Mockito.when(agendaFuncionarioService.criar(Mockito.any(AgendaFuncionarioDto.class))).thenReturn(agenda(1L));

        mockMvc.perform(post("/agenda-funcionario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Unidade: AgendaFuncionarioController | Cenario: put agenda funcionario | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void putAgendaFuncionario_deveRetornar200() throws Exception {
        Mockito.when(agendaFuncionarioService.atualizar(Mockito.eq(1L), Mockito.any(AgendaFuncionarioDto.class)))
                .thenReturn(agenda(1L));

        mockMvc.perform(put("/agenda-funcionario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Unidade: AgendaFuncionarioController | Cenario: delete agenda funcionario | Dados: dados preparados no arrange do teste | Verifica: deve retornar 204")
    @Test
    void deleteAgendaFuncionario_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/agenda-funcionario/1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Unidade: AgendaFuncionarioController | Cenario: get excecoes | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getExcecoes_deveRetornar200() throws Exception {
        Mockito.when(agendaFuncionarioService.listarExcecoesPorFuncionario(1L)).thenReturn(List.of(excecao(1L)));

        mockMvc.perform(get("/agenda-funcionario/1/excecoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @DisplayName("Unidade: AgendaFuncionarioController | Cenario: post excecoes | Dados: dados preparados no arrange do teste | Verifica: deve retornar 201")
    @Test
    void postExcecoes_deveRetornar201() throws Exception {
        Mockito.when(agendaFuncionarioService.criarExcecao(Mockito.any(AgendaExcecaoDto.class))).thenReturn(excecao(1L));

        mockMvc.perform(post("/agenda-funcionario/excecoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(excecaoDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Unidade: AgendaFuncionarioController | Cenario: delete excecoes | Dados: dados preparados no arrange do teste | Verifica: deve retornar 204")
    @Test
    void deleteExcecoes_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/agenda-funcionario/excecoes/1"))
                .andExpect(status().isNoContent());
    }

    private AgendaFuncionarioDto agendaDto() {
        AgendaFuncionarioDto dto = new AgendaFuncionarioDto();
        dto.setFuncionarioId(1L);
        dto.setDiaSemana(2);
        dto.setHoraInicio(LocalTime.of(8, 0));
        dto.setHoraFim(LocalTime.of(12, 0));
        return dto;
    }

    private AgendaExcecaoDto excecaoDto() {
        AgendaExcecaoDto dto = new AgendaExcecaoDto();
        dto.setFuncionarioId(1L);
        dto.setData(LocalDate.of(2026, 6, 9));
        dto.setHoraInicio(LocalTime.of(8, 0));
        dto.setHoraFim(LocalTime.of(12, 0));
        dto.setDisponivel(false);
        return dto;
    }

    private AgendaFuncionario agenda(Long id) {
        AgendaFuncionario agenda = new AgendaFuncionario();
        agenda.setId(id);
        agenda.setDiaSemana(2);
        agenda.setHoraInicio(LocalTime.of(8, 0));
        agenda.setHoraFim(LocalTime.of(12, 0));
        return agenda;
    }

    private AgendaExcecao excecao(Long id) {
        AgendaExcecao excecao = new AgendaExcecao();
        excecao.setId(id);
        excecao.setData(LocalDate.of(2026, 6, 9));
        excecao.setHoraInicio(LocalTime.of(8, 0));
        excecao.setHoraFim(LocalTime.of(12, 0));
        excecao.setDisponivel(false);
        return excecao;
    }
}
