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
import sptech.school.backend.dto.DisponibilidadeDto.DiaDisponivelDto;
import sptech.school.backend.dto.DisponibilidadeDto.HorarioDisponivelDto;
import sptech.school.backend.dto.DisponibilidadeDto.SalaDisponibilidadeDto;
import sptech.school.backend.service.DisponibilidadeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - DisponibilidadeController")
class DisponibilidadeControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private DisponibilidadeController disponibilidadeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(disponibilidadeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private DisponibilidadeService disponibilidadeService;

    @DisplayName("Unidade: DisponibilidadeController | Cenario: get calendario | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getCalendario_deveRetornar200() throws Exception {
        Mockito.when(disponibilidadeService.calcularCalendario("2026-06"))
                .thenReturn(List.of(new DiaDisponivelDto("2026-06-09", "disponivel")));

        mockMvc.perform(get("/disponibilidade/calendario").param("mes", "2026-06"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("disponivel"));
    }

    @DisplayName("Unidade: DisponibilidadeController | Cenario: get horarios | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getHorarios_deveRetornar200() throws Exception {
        Mockito.when(disponibilidadeService.calcularHorariosDisponiveis(LocalDate.of(2026, 6, 9)))
                .thenReturn(List.of(new HorarioDisponivelDto("08:00", true)));

        mockMvc.perform(get("/disponibilidade/horarios").param("data", "2026-06-09"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].horario").value("08:00"));
    }

    @DisplayName("Unidade: DisponibilidadeController | Cenario: get salas | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getSalas_deveRetornar200() throws Exception {
        Mockito.when(disponibilidadeService.verificarDisponibilidadeSalas(
                LocalDateTime.of(2026, 6, 9, 8, 0),
                LocalDateTime.of(2026, 6, 9, 9, 0)
        )).thenReturn(List.of(new SalaDisponibilidadeDto(1L, "Sala 1", false)));

        mockMvc.perform(get("/disponibilidade/salas")
                        .param("inicio", "2026-06-09T08:00:00")
                        .param("fim", "2026-06-09T09:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Sala 1"));
    }
}
