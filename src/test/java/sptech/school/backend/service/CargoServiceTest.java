package sptech.school.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.dto.CargoDto.CargoCriacaoDto;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.entity.Permissao;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.CargoRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.PermissaoRepository;
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
            List<Cargo> listaVazia = List.of();

            Mockito.when(repository.findAll()).thenReturn(listaVazia);

            List<Cargo> resultado = service.listar();

            Assertions.assertTrue(resultado.isEmpty());
            Mockito.verify(repository).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista com dados quando houver dados")
        void deveRetornarListaComDadosQuandoHouverDados() {
            List<Cargo> listaEsperada = List.of(
                    new Cargo(),
                    new Cargo(),
                    new Cargo()
            );

            Mockito.when(repository.findAll()).thenReturn(listaEsperada);

            List<Cargo> resultado = service.listar();

            Assertions.assertEquals(listaEsperada, resultado);
            Mockito.verify(repository).findAll();
        }
    }

    @Nested
    @DisplayName("Testes de busca por ID")
    class CargoServiceBuscarPorIdTeste {

        @Test
        @DisplayName("Deve retornar cargo quando id existir")
        void deveRetornarCorretamente() {
            Cargo entidade = new Cargo();
            entidade.setId(1L);
            entidade.setNome("Administrador");
            entidade.setDescricao("Acesso total ao sistema");

            Mockito.when(repository.findById(1L)).thenReturn(Optional.of(entidade));

            Cargo cargo = service.buscarPorId(1L);

            Assertions.assertEquals(entidade, cargo);
            Mockito.verify(repository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando id não existir")
        void deveLancarExceptionCorretamente() {
            Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

            Assertions.assertThrows(
                    RecursoNaoEncontradoException.class,
                    () -> service.buscarPorId(1L)
            );

            Mockito.verify(repository).findById(1L);
        }
    }

    @Nested
    @DisplayName("Testes de criação")
    class CargoServiceCriarTeste {

        @Test
        @DisplayName("Deve criar cargo com permissões")
        void deveCriarCargoComPermissoes() {
            CargoCriacaoDto dto = criarDto("Administrador", "Acesso total", List.of(1L, 1L, 2L));
            List<Permissao> permissoes = List.of(criarPermissao(1L, "Ver agenda"), criarPermissao(2L, "Editar agenda"));

            Mockito.when(permissaoRepository.findAllById(List.of(1L, 2L))).thenReturn(permissoes);
            Mockito.when(repository.save(Mockito.any(Cargo.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Cargo resultado = service.criar(dto);

            Assertions.assertEquals("Administrador", resultado.getNome());
            Assertions.assertEquals("Acesso total", resultado.getDescricao());
            Assertions.assertEquals(permissoes, resultado.getPermissoes());
            Mockito.verify(permissaoRepository).findAllById(List.of(1L, 2L));
            Mockito.verify(repository).save(Mockito.any(Cargo.class));
        }

        @Test
        @DisplayName("Deve criar cargo sem permissões quando lista for nula")
        void deveCriarCargoSemPermissoesQuandoListaForNula() {
            CargoCriacaoDto dto = criarDto("Recepcionista", "Atendimento", null);

            Mockito.when(repository.save(Mockito.any(Cargo.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Cargo resultado = service.criar(dto);

            Assertions.assertEquals("Recepcionista", resultado.getNome());
            Assertions.assertEquals("Atendimento", resultado.getDescricao());
            Assertions.assertTrue(resultado.getPermissoes().isEmpty());
            Mockito.verifyNoInteractions(permissaoRepository);
            Mockito.verify(repository).save(Mockito.any(Cargo.class));
        }

        @Test
        @DisplayName("Deve criar cargo sem permissões quando lista estiver vazia")
        void deveCriarCargoSemPermissoesQuandoListaEstiverVazia() {
            CargoCriacaoDto dto = criarDto("Auxiliar", "Suporte", List.of());

            Mockito.when(repository.save(Mockito.any(Cargo.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Cargo resultado = service.criar(dto);

            Assertions.assertEquals("Auxiliar", resultado.getNome());
            Assertions.assertEquals("Suporte", resultado.getDescricao());
            Assertions.assertTrue(resultado.getPermissoes().isEmpty());
            Mockito.verifyNoInteractions(permissaoRepository);
            Mockito.verify(repository).save(Mockito.any(Cargo.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando alguma permissão não existir")
        void deveLancarExceptionQuandoPermissaoNaoExistir() {
            CargoCriacaoDto dto = criarDto("Administrador", "Acesso total", List.of(1L, 2L));

            Mockito.when(permissaoRepository.findAllById(List.of(1L, 2L)))
                    .thenReturn(List.of(criarPermissao(1L, "Ver agenda")));

            Assertions.assertThrows(
                    RecursoNaoEncontradoException.class,
                    () -> service.criar(dto)
            );

            Mockito.verify(permissaoRepository).findAllById(List.of(1L, 2L));
            Mockito.verify(repository, Mockito.never()).save(Mockito.any(Cargo.class));
        }
    }

    @Nested
    @DisplayName("Testes de atualização")
    class CargoServiceAtualizarTeste {

        @Test
        @DisplayName("Deve atualizar cargo existente")
        void deveAtualizarCargoExistente() {
            Cargo cargoExistente = criarCargo(1L, "Antigo", "Descrição antiga");
            CargoCriacaoDto dto = criarDto("Novo", "Descrição nova", List.of(3L));
            List<Permissao> permissoes = List.of(criarPermissao(3L, "Criar usuário"));

            Mockito.when(repository.findById(1L)).thenReturn(Optional.of(cargoExistente));
            Mockito.when(permissaoRepository.findAllById(List.of(3L))).thenReturn(permissoes);
            Mockito.when(repository.save(Mockito.any(Cargo.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Cargo resultado = service.atualizar(1L, dto);

            Assertions.assertEquals(1L, resultado.getId());
            Assertions.assertEquals("Novo", resultado.getNome());
            Assertions.assertEquals("Descrição nova", resultado.getDescricao());
            Assertions.assertEquals(permissoes, resultado.getPermissoes());
            Mockito.verify(repository).findById(1L);
            Mockito.verify(permissaoRepository).findAllById(List.of(3L));
            Mockito.verify(repository).save(cargoExistente);
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar cargo inexistente")
        void deveLancarExceptionAoAtualizarCargoInexistente() {
            CargoCriacaoDto dto = criarDto("Novo", "Descrição nova", List.of(1L));

            Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

            Assertions.assertThrows(
                    RecursoNaoEncontradoException.class,
                    () -> service.atualizar(1L, dto)
            );

            Mockito.verify(repository).findById(1L);
            Mockito.verifyNoInteractions(permissaoRepository);
            Mockito.verify(repository, Mockito.never()).save(Mockito.any(Cargo.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com permissão inexistente")
        void deveLancarExceptionAoAtualizarComPermissaoInexistente() {
            Cargo cargoExistente = criarCargo(1L, "Antigo", "Descrição antiga");
            CargoCriacaoDto dto = criarDto("Novo", "Descrição nova", List.of(1L, 2L));

            Mockito.when(repository.findById(1L)).thenReturn(Optional.of(cargoExistente));
            Mockito.when(permissaoRepository.findAllById(List.of(1L, 2L)))
                    .thenReturn(List.of(criarPermissao(1L, "Ver agenda")));

            Assertions.assertThrows(
                    RecursoNaoEncontradoException.class,
                    () -> service.atualizar(1L, dto)
            );

            Mockito.verify(repository).findById(1L);
            Mockito.verify(permissaoRepository).findAllById(List.of(1L, 2L));
            Mockito.verify(repository, Mockito.never()).save(Mockito.any(Cargo.class));
        }
    }

    @Nested
    @DisplayName("Testes de deleção")
    class CargoServiceDeletarTeste {

        @Test
        @DisplayName("Deve deletar cargo quando id existir")
        void deveDeletarCargoQuandoIdExistir() {
            Mockito.when(repository.existsById(1L)).thenReturn(true);

            service.deletar(1L);

            Mockito.verify(repository).existsById(1L);
            Mockito.verify(repository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando id não existir")
        void deveLancarExceptionQuandoIdNaoExistir() {
            Mockito.when(repository.existsById(1L)).thenReturn(false);

            Assertions.assertThrows(
                    RecursoNaoEncontradoException.class,
                    () -> service.deletar(1L)
            );

            Mockito.verify(repository).existsById(1L);
            Mockito.verify(repository, Mockito.never()).deleteById(Mockito.anyLong());
        }
    }

    @Nested
    @DisplayName("Testes dos dados salvos")
    class CargoServiceDadosSalvosTeste {

        @Test
        @DisplayName("Deve enviar cargo preenchido para o repositório ao criar")
        void deveEnviarCargoPreenchidoParaRepositorioAoCriar() {
            CargoCriacaoDto dto = criarDto("Gestor", "Gestão da clínica", List.of(1L));
            List<Permissao> permissoes = List.of(criarPermissao(1L, "Gerenciar cargos"));
            ArgumentCaptor<Cargo> captor = ArgumentCaptor.forClass(Cargo.class);

            Mockito.when(permissaoRepository.findAllById(List.of(1L))).thenReturn(permissoes);
            Mockito.when(repository.save(Mockito.any(Cargo.class))).thenAnswer(invocation -> invocation.getArgument(0));

            service.criar(dto);

            Mockito.verify(repository).save(captor.capture());
            Cargo cargoSalvo = captor.getValue();

            Assertions.assertNull(cargoSalvo.getId());
            Assertions.assertEquals("Gestor", cargoSalvo.getNome());
            Assertions.assertEquals("Gestão da clínica", cargoSalvo.getDescricao());
            Assertions.assertEquals(permissoes, cargoSalvo.getPermissoes());
        }
    }

    private CargoCriacaoDto criarDto(String nome, String descricao, List<Long> permissoesIds) {
        CargoCriacaoDto dto = new CargoCriacaoDto();
        dto.setNome(nome);
        dto.setDescricao(descricao);
        dto.setPermissoesIds(permissoesIds);
        return dto;
    }

    private Cargo criarCargo(Long id, String nome, String descricao) {
        Cargo cargo = new Cargo();
        cargo.setId(id);
        cargo.setNome(nome);
        cargo.setDescricao(descricao);
        return cargo;
    }

    private Permissao criarPermissao(Long id, String nome) {
        Permissao permissao = new Permissao();
        permissao.setId(id);
        permissao.setNome(nome);
        return permissao;
    }
}
