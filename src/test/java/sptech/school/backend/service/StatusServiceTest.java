package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.entity.Status;
import sptech.school.backend.repository.StatusRepository;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - StatusService")
class StatusServiceTest {

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusService service;

    @DisplayName("Unidade: StatusService | Cenario: listar | Dados: dados preparados no arrange do teste | Verifica: deve retornar todos os status")
    @Test
    void listar_deveRetornarTodosOsStatus() {
        List<Status> statuses = List.of(status(1L, "Agendado"));
        Mockito.when(statusRepository.findAll()).thenReturn(statuses);

        List<Status> resultado = service.listar();

        Assertions.assertEquals(statuses, resultado);
    }

    private Status status(Long id, String nome) {
        Status status = new Status();
        status.setId(id);
        status.setNome(nome);
        return status;
    }
}
