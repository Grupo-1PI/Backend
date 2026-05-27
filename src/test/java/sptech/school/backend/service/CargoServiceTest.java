package sptech.school.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.CargoRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.PermissaoRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CargoServiceTest {

    @Mock
    private CargoRepository repository;

    @Mock
    private PermissaoRepository permissaoRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private CargoService service;

    @Nested
    @DisplayName("Testes de listagem")
    class CargoServiceListarTeste {

        @Test
        @DisplayName("Deve retornar uma lista vazia quando não houver dados")
        void deveRetornarListaVaziaQuandoNaoHaDados() {
            //Given
            var listaVazia = Collections.EMPTY_LIST;

            //When
            Mockito.when(repository.findAll()).thenReturn(listaVazia);

            //Then
            List<Cargo> resultado = service.listar();

            Assertions.assertTrue(resultado.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar lista com dados quando houver dados")
        void deveRetornarListaComDadosQuandoHouverDados() {
            //Given
            List<Cargo> listaEsperada = List.of(
                    new Cargo(),
                    new Cargo(),
                    new Cargo()
            );

            //When
            Mockito.when(repository.findAll()).thenReturn(listaEsperada);

            //Then
            List<Cargo> resultado = service.listar();

            Assertions.assertFalse(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Buscar por ID")
    class CargoServiceBuscarPorIdTeste {

        @Test
        @DisplayName("Deve retornar simulação quando id válido")
        void deveRetornarCorretamente() {
            //Given
            Cargo entidade = new Cargo();
            entidade.setId(1L);
            entidade.setNome("Administrador");
            entidade.setDescricao("Acesso total ao sistema");

            Optional<Cargo> optEntidade = Optional.of(entidade);

            //When
            Mockito.when(repository.findById(Mockito.anyLong()))
                    .thenReturn(optEntidade);

            //Then
            Cargo cargo = service.buscarPorId(1L);

            Assertions.assertNotNull(cargo);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFound exception quando o id for inválido")
        void deveLancarExceptionCorretamente() {
            //Given
            Optional<Cargo> empty = Optional.empty();

            //When
            Mockito.when(repository.findById(Mockito.anyLong()))
                    .thenReturn(empty);

            //Then
            Assertions.assertThrows(
                    RecursoNaoEncontradoException.class,
                    () -> service.buscarPorId(1L)
            );
        }
    }
}
