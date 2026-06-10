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
import sptech.school.backend.dto.CargoDto.CargoCriacaoDto;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.service.CargoService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - CargoController")
class CargoControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private CargoController cargoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cargoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private CargoService cargoService;

    @DisplayName("Unidade: CargoController | Cenario: get cargos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getCargos_deveRetornar200() throws Exception {
        Mockito.when(cargoService.listar()).thenReturn(List.of(cargo(1L, "Administrador")));

        mockMvc.perform(get("/cargos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Administrador"));
    }

    @DisplayName("Unidade: CargoController | Cenario: get cargo por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getCargoPorId_deveRetornar200() throws Exception {
        Mockito.when(cargoService.buscarPorId(1L)).thenReturn(cargo(1L, "Administrador"));

        mockMvc.perform(get("/cargos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Unidade: CargoController | Cenario: get cargo por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 404")
    @Test
    void getCargoPorId_deveRetornar404() throws Exception {
        Mockito.when(cargoService.buscarPorId(999L)).thenThrow(new RecursoNaoEncontradoException("Cargo nao encontrado"));

        mockMvc.perform(get("/cargos/999"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Unidade: CargoController | Cenario: post cargos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 201")
    @Test
    void postCargos_deveRetornar201() throws Exception {
        Mockito.when(cargoService.criar(Mockito.any(CargoCriacaoDto.class))).thenReturn(cargo(1L, "Cargo Novo"));

        mockMvc.perform(post("/cargos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Cargo Novo"));
    }

    @DisplayName("Unidade: CargoController | Cenario: put cargos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void putCargos_deveRetornar200() throws Exception {
        Mockito.when(cargoService.atualizar(Mockito.eq(1L), Mockito.any(CargoCriacaoDto.class)))
                .thenReturn(cargo(1L, "Cargo Editado"));

        mockMvc.perform(put("/cargos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cargo Editado"));
    }

    @DisplayName("Unidade: CargoController | Cenario: delete cargos | Dados: dados preparados no arrange do teste | Verifica: deve retornar 204")
    @Test
    void deleteCargos_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/cargos/1"))
                .andExpect(status().isNoContent());
    }

    private CargoCriacaoDto dto() {
        CargoCriacaoDto dto = new CargoCriacaoDto();
        dto.setNome("Cargo");
        dto.setDescricao("Descricao");
        dto.setPermissoesIds(List.of(1L));
        return dto;
    }

    private Cargo cargo(Long id, String nome) {
        Cargo cargo = new Cargo();
        cargo.setId(id);
        cargo.setNome(nome);
        return cargo;
    }
}
