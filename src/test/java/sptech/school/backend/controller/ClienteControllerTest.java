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
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.service.ClienteService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unitario - ClienteController")
class ClienteControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Mock
    private ClienteService clienteService;

    @DisplayName("Unidade: ClienteController | Cenario: get clientes | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getClientes_deveRetornar200() throws Exception {
        Mockito.when(clienteService.listar()).thenReturn(List.of(cliente(1L)));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Cliente"));
    }

    @DisplayName("Unidade: ClienteController | Cenario: get cliente por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 200")
    @Test
    void getClientePorId_deveRetornar200() throws Exception {
        Mockito.when(clienteService.buscarPorId(1L)).thenReturn(cliente(1L));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Unidade: ClienteController | Cenario: get cliente por ID | Dados: dados preparados no arrange do teste | Verifica: deve retornar 404")
    @Test
    void getClientePorId_deveRetornar404() throws Exception {
        Mockito.when(clienteService.buscarPorId(999L)).thenThrow(new RecursoNaoEncontradoException("Cliente nao encontrado"));

        mockMvc.perform(get("/clientes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Cliente nao encontrado"));
    }

    private Cliente cliente(Long id) {
        Usuario usuario = new Usuario();
        usuario.setNome("Cliente");
        usuario.setEmail("cliente@email.com");
        usuario.setTelefone("11999990000");

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setUsuario(usuario);
        cliente.setObservacao("Observacao");
        return cliente;
    }
}
