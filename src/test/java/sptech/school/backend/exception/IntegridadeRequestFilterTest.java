package sptech.school.backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Status;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.service.AgendamentoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:integridade;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never"
})
class IntegridadeRequestFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendamentoService agendamentoService;

    @Test
    void filtro_deveRejeitar_quandoPostSemBody() throws Exception {
        mockMvc.perform(post("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"mensagem\": \"Request body não pode ser vazio\"}"));
    }

    @Test
    void filtro_deveRejeitar_quandoContentTypeErrado() throws Exception {
        mockMvc.perform(post("/agendamentos")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("body"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void filtro_devePermitir_quandoPostComBodyValido() throws Exception {
        when(agendamentoService.criar(any(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong()))
                .thenReturn(agendamentoResposta());

        mockMvc.perform(post("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dataHoraInicio": "2026-05-01T14:00:00",
                                  "dataHoraFim": "2026-05-01T15:00:00",
                                  "observacao": "Sessao inicial",
                                  "clienteId": 1,
                                  "funcionarioId": 1,
                                  "salaId": 1,
                                  "servicoId": 1,
                                  "statusId": 1
                                }
                                """))
                .andExpect(status().is(not(is(400))))
                .andExpect(status().is(not(is(415))));
    }

    private Agendamento agendamentoResposta() {
        Usuario usuario = new Usuario();
        usuario.setNome("Cliente Teste");

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);

        Sala sala = new Sala();
        sala.setDescricao("Sala 1");

        Status status = new Status();
        status.setNome("Agendado");

        Agendamento agendamento = new Agendamento();
        agendamento.setId(1L);
        agendamento.setDataHoraInicio(LocalDateTime.of(2026, 5, 1, 14, 0));
        agendamento.setDataHoraFim(LocalDateTime.of(2026, 5, 1, 15, 0));
        agendamento.setObservacao("Sessao inicial");
        agendamento.setCliente(cliente);
        agendamento.setSala(sala);
        agendamento.setStatus(status);
        agendamento.setFuncionarioAgendamentos(List.of());
        agendamento.setAtendimentoServicos(List.of());
        return agendamento;
    }
}
