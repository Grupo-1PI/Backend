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
import sptech.school.backend.dto.AgendamentoDto.AgendamentoRequestDto;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.entity.AtendimentoServico;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.FuncionarioAgendamento;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.entity.Status;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.exception.ConflitoException;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.service.AgendamentoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - AgendamentoController")
class AgendamentoControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private AgendamentoController agendamentoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(agendamentoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private AgendamentoService agendamentoService;

    @DisplayName("Unidade: AgendamentoController | Cenario: get agendamentos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getAgendamentos_deveRetornar200() throws Exception {
        Mockito.when(agendamentoService.listar()).thenReturn(List.of(agendamento(1L)));

        mockMvc.perform(get("/agendamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteNome").value("Cliente"));
    }

    @DisplayName("Unidade: AgendamentoController | Cenario: get agendamentos por status | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getAgendamentosPorStatus_deveRetornar200() throws Exception {
        Mockito.when(agendamentoService.listarPorStatus(1L)).thenReturn(List.of(agendamento(1L)));

        mockMvc.perform(get("/agendamentos").param("statusId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].statusNome").value("Agendado"));
    }

    @DisplayName("Unidade: AgendamentoController | Cenario: get agendamentos por periodo | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getAgendamentosPorPeriodo_deveRetornar200() throws Exception {
        Mockito.when(agendamentoService.listarPorPeriodo(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(agendamento(1L)));

        mockMvc.perform(get("/agendamentos")
                        .param("inicio", "2026-06-09T08:00:00")
                        .param("fim", "2026-06-09T09:00:00"))
                .andExpect(status().isOk());
    }

    @DisplayName("Unidade: AgendamentoController | Cenario: get agendamentos por periodo e status | Dados: dados preparados no arrange do teste | Verifica: deve filtrar status")
    @Test
    void getAgendamentosPorPeriodoEStatus_deveFiltrarStatus() throws Exception {
        Mockito.when(agendamentoService.listarPorPeriodo(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(agendamento(1L, 1L), agendamento(2L, 2L)));

        mockMvc.perform(get("/agendamentos")
                        .param("inicio", "2026-06-09T08:00:00")
                        .param("fim", "2026-06-09T10:00:00")
                        .param("statusId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @DisplayName("Unidade: AgendamentoController | Cenario: get agendamentos | Dados: quando inicio sem fim | Verifica: deve retornar 400")
    @Test
    void getAgendamentos_deveRetornar400_quandoInicioSemFim() throws Exception {
        mockMvc.perform(get("/agendamentos")
                        .param("inicio", "2026-06-09T08:00:00"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Unidade: AgendamentoController | Cenario: get agendamento por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getAgendamentoPorId_deveRetornar200() throws Exception {
        Mockito.when(agendamentoService.buscarPorId(1L)).thenReturn(agendamento(1L));

        mockMvc.perform(get("/agendamentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Unidade: AgendamentoController | Cenario: get agendamento por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 404")
    @Test
    void getAgendamentoPorId_deveRetornar404() throws Exception {
        Mockito.when(agendamentoService.buscarPorId(999L))
                .thenThrow(new RecursoNaoEncontradoException("Agendamento nao encontrado"));

        mockMvc.perform(get("/agendamentos/999"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Unidade: AgendamentoController | Cenario: get meus agendamentos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getMeusAgendamentos_deveRetornar200() throws Exception {
        Mockito.when(agendamentoService.listarPorCliente(1L)).thenReturn(List.of(agendamento(1L)));

        mockMvc.perform(get("/agendamentos/meus/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @DisplayName("Unidade: AgendamentoController | Cenario: post agendamentos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 201")
    @Test
    void postAgendamentos_deveRetornar201() throws Exception {
        Mockito.when(agendamentoService.criar(
                Mockito.any(Agendamento.class),
                Mockito.eq(1L),
                Mockito.eq(1L),
                Mockito.eq(1L),
                Mockito.eq(1L),
                Mockito.eq(1L)
        )).thenReturn(agendamento(1L));

        mockMvc.perform(post("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Unidade: AgendamentoController | Cenario: post agendamentos | Dados: com conflito | Verifica: deve retornar 409")
    @Test
    void postAgendamentos_comConflito_deveRetornar409() throws Exception {
        Mockito.when(agendamentoService.criar(
                Mockito.any(Agendamento.class),
                Mockito.eq(1L),
                Mockito.eq(1L),
                Mockito.eq(1L),
                Mockito.eq(1L),
                Mockito.eq(1L)
        )).thenThrow(new ConflitoException("Conflito"));

        mockMvc.perform(post("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Conflito"));
    }

    @DisplayName("Unidade: AgendamentoController | Cenario: put agendamentos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void putAgendamentos_deveRetornar200() throws Exception {
        Mockito.when(agendamentoService.atualizar(
                Mockito.eq(1L),
                Mockito.any(Agendamento.class),
                Mockito.eq(1L),
                Mockito.eq(1L),
                Mockito.eq(1L),
                Mockito.eq(1L),
                Mockito.eq(1L)
        )).thenReturn(agendamento(1L));

        mockMvc.perform(put("/agendamentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Unidade: AgendamentoController | Cenario: delete agendamentos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 204")
    @Test
    void deleteAgendamentos_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/agendamentos/1"))
                .andExpect(status().isNoContent());
    }

    private AgendamentoRequestDto dto() {
        AgendamentoRequestDto dto = new AgendamentoRequestDto();
        dto.setDataHoraInicio(LocalDateTime.of(2026, 6, 9, 8, 0));
        dto.setDataHoraFim(LocalDateTime.of(2026, 6, 9, 9, 0));
        dto.setObservacao("Observacao");
        dto.setClienteId(1L);
        dto.setFuncionarioId(1L);
        dto.setSalaId(1L);
        dto.setServicoId(1L);
        dto.setStatusId(1L);
        return dto;
    }

    private Agendamento agendamento(Long id) {
        return agendamento(id, 1L);
    }

    private Agendamento agendamento(Long id, Long statusId) {
        Usuario usuarioCliente = new Usuario();
        usuarioCliente.setNome("Cliente");
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuarioCliente);

        Usuario usuarioFuncionario = new Usuario();
        usuarioFuncionario.setNome("Funcionario");
        Funcionario funcionario = new Funcionario();
        funcionario.setUsuario(usuarioFuncionario);
        FuncionarioAgendamento funcionarioAgendamento = new FuncionarioAgendamento();
        funcionarioAgendamento.setFuncionario(funcionario);

        Sala sala = new Sala();
        sala.setDescricao("Sala 1");

        Servico servico = new Servico();
        servico.setNome("Servico");
        AtendimentoServico atendimentoServico = new AtendimentoServico();
        atendimentoServico.setServico(servico);

        Status status = new Status();
        status.setId(statusId);
        status.setNome("Agendado");

        Agendamento agendamento = new Agendamento();
        agendamento.setId(id);
        agendamento.setDataHoraInicio(LocalDateTime.of(2026, 6, 9, 8, 0));
        agendamento.setDataHoraFim(LocalDateTime.of(2026, 6, 9, 9, 0));
        agendamento.setObservacao("Observacao");
        agendamento.setCliente(cliente);
        agendamento.setSala(sala);
        agendamento.setStatus(status);
        agendamento.getFuncionarioAgendamentos().add(funcionarioAgendamento);
        agendamento.getAtendimentoServicos().add(atendimentoServico);
        return agendamento;
    }
}
