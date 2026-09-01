package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - ClienteService")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService service;

    @DisplayName("Unidade: ClienteService | Cenario: listar | Dados: dados preparados no arrange do teste | Verifica: deve retornar lista")
    @Test
    void listar_deveRetornarLista() {
        List<Cliente> clientes = List.of(cliente(1L));
        Mockito.when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> resultado = service.listar();

        Assertions.assertEquals(clientes, resultado);
    }

    @DisplayName("Unidade: ClienteService | Cenario: buscar por ID | Dados: quando existe | Verifica: deve retornar")
    @Test
    void buscarPorId_deveRetornar_quandoExiste() {
        Cliente cliente = cliente(1L);
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = service.buscarPorId(1L);

        Assertions.assertEquals(cliente, resultado);
    }

    @DisplayName("Unidade: ClienteService | Cenario: buscar por ID | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void buscarPorId_deveLancar_quandoNaoExiste() {
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(1L));
    }

    @DisplayName("Unidade: ClienteService | Cenario: buscar por e-mail | Dados: cliente existente | Verifica: deve retornar")
    @Test
    void buscarPorEmailUsuario_deveRetornar_quandoExiste() {
        Cliente cliente = cliente(1L);
        Mockito.when(clienteRepository.findByUsuarioEmail("cliente@email.com")).thenReturn(Optional.of(cliente));

        Cliente resultado = service.buscarPorEmailUsuario("cliente@email.com");

        Assertions.assertEquals(cliente, resultado);
    }

    @DisplayName("Unidade: ClienteService | Cenario: buscar por e-mail | Dados: cliente inexistente | Verifica: deve lancar")
    @Test
    void buscarPorEmailUsuario_deveLancar_quandoNaoExiste() {
        Mockito.when(clienteRepository.findByUsuarioEmail("cliente@email.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarPorEmailUsuario("cliente@email.com"));
    }

    private Cliente cliente(Long id) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        return cliente;
    }
}
