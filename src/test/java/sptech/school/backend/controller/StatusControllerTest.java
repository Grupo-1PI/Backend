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
import sptech.school.backend.entity.Status;
import sptech.school.backend.service.StatusService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - StatusController")
class StatusControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private StatusController statusController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statusController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private StatusService statusService;

    @DisplayName("Unidade: StatusController | Cenario: get status | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getStatus_deveRetornar200() throws Exception {
        Mockito.when(statusService.listar()).thenReturn(List.of(statusEntity(1L, "Agendado")));

        mockMvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Agendado"));
    }

    private Status statusEntity(Long id, String nome) {
        Status status = new Status();
        status.setId(id);
        status.setNome(nome);
        return status;
    }
}
