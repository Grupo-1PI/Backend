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
import sptech.school.backend.dto.FuncionarioDto.FuncionarioAtualizacaoDto;
import sptech.school.backend.dto.FuncionarioDto.FuncionarioCriacaoDto;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.service.FuncionarioService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - FuncionarioController")
class FuncionarioControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private FuncionarioController funcionarioController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(funcionarioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private FuncionarioService funcionarioService;

    @DisplayName("Unidade: FuncionarioController | Cenario: get funcionarios | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getFuncionarios_deveRetornar200() throws Exception {
        Mockito.when(funcionarioService.listar()).thenReturn(List.of(funcionario(1L, "Funcionario")));

        mockMvc.perform(get("/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Funcionario"));
    }

    @DisplayName("Unidade: FuncionarioController | Cenario: get funcionario por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getFuncionarioPorId_deveRetornar200() throws Exception {
        Mockito.when(funcionarioService.buscarPorId(1L)).thenReturn(funcionario(1L, "Funcionario"));

        mockMvc.perform(get("/funcionarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Unidade: FuncionarioController | Cenario: get funcionario por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 404")
    @Test
    void getFuncionarioPorId_deveRetornar404() throws Exception {
        Mockito.when(funcionarioService.buscarPorId(999L))
                .thenThrow(new RecursoNaoEncontradoException("Funcionario nao encontrado"));

        mockMvc.perform(get("/funcionarios/999"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Unidade: FuncionarioController | Cenario: post funcionarios | Dados: dados preparados no arrange do teste | Verifica: deve retornar 201")
    @Test
    void postFuncionarios_deveRetornar201() throws Exception {
        Mockito.when(funcionarioService.criar(Mockito.any(FuncionarioCriacaoDto.class)))
                .thenReturn(funcionario(1L, "Funcionario"));

        mockMvc.perform(post("/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criacaoDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Funcionario"));
    }

    @DisplayName("Unidade: FuncionarioController | Cenario: put funcionarios | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void putFuncionarios_deveRetornar200() throws Exception {
        Mockito.when(funcionarioService.atualizar(Mockito.eq(1L), Mockito.any(FuncionarioAtualizacaoDto.class)))
                .thenReturn(funcionario(1L, "Funcionario Editado"));

        mockMvc.perform(put("/funcionarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizacaoDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Funcionario Editado"));
    }

    @DisplayName("Unidade: FuncionarioController | Cenario: delete funcionarios | Dados: dados preparados no arrange do teste | Verifica: deve retornar 204")
    @Test
    void deleteFuncionarios_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/funcionarios/1"))
                .andExpect(status().isNoContent());
    }

    private FuncionarioCriacaoDto criacaoDto() {
        FuncionarioCriacaoDto dto = new FuncionarioCriacaoDto();
        dto.setNome("Funcionario");
        dto.setEmail("funcionario@email.com");
        dto.setTelefone("11999990000");
        dto.setSenha("123456");
        dto.setDataNascimento(LocalDate.of(1990, 1, 1));
        dto.setEndereco(enderecoDto());
        dto.setCargoId(1L);
        dto.setEspecialidadesIds(List.of(1L));
        return dto;
    }

    private FuncionarioAtualizacaoDto atualizacaoDto() {
        FuncionarioAtualizacaoDto dto = new FuncionarioAtualizacaoDto();
        dto.setCargoId(1L);
        dto.setEspecialidadesIds(List.of(1L));
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

    private Funcionario funcionario(Long id, String nome) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail("funcionario@email.com");
        usuario.setTelefone("11999990000");

        Cargo cargo = new Cargo();
        cargo.setNome("Cargo");

        Funcionario funcionario = new Funcionario();
        funcionario.setId(id);
        funcionario.setUsuario(usuario);
        funcionario.setCargo(cargo);
        return funcionario;
    }
}
