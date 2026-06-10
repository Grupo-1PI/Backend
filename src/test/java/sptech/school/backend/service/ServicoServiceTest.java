package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.dto.ServicoDto.ServicoCriacaoDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.SalaRepository;
import sptech.school.backend.repository.ServicoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - ServicoService")
class ServicoServiceTest {

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private ServicoService service;

    @DisplayName("Unidade: ServicoService | Cenario: listar | Dados: dados preparados no arrange do teste | Verifica: deve retornar lista")
    @Test
    void listar_deveRetornarLista() {
        List<Servico> servicos = List.of(servico(1L));
        Mockito.when(servicoRepository.findAll()).thenReturn(servicos);

        List<Servico> resultado = service.listar();

        Assertions.assertEquals(servicos, resultado);
    }

    @DisplayName("Unidade: ServicoService | Cenario: buscar por ID | Dados: quando existe | Verifica: deve retornar")
    @Test
    void buscarPorId_deveRetornar_quandoExiste() {
        Servico servico = servico(1L);
        Mockito.when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));

        Servico resultado = service.buscarPorId(1L);

        Assertions.assertEquals(servico, resultado);
    }

    @DisplayName("Unidade: ServicoService | Cenario: buscar por ID | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void buscarPorId_deveLancar_quandoNaoExiste() {
        Mockito.when(servicoRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(1L));
    }

    @DisplayName("Unidade: ServicoService | Cenario: criar | Dados: com salas | Verifica: deve vincular e salvar")
    @Test
    void criar_comSalas_deveVincularESalvar() {
        ServicoCriacaoDto dto = dto(List.of(1L, 2L));
        List<Sala> salas = List.of(sala(1L, "Sala 1"), sala(2L, "Sala 2"));
        Mockito.when(salaRepository.findAllById(List.of(1L, 2L))).thenReturn(salas);
        Mockito.when(servicoRepository.save(Mockito.any(Servico.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Servico resultado = service.criar(dto);

        Assertions.assertEquals("Servico", resultado.getNome());
        Assertions.assertEquals(salas, resultado.getSalas());
    }

    @DisplayName("Unidade: ServicoService | Cenario: criar | Dados: sem salas | Verifica: deve salvar sem vincular")
    @Test
    void criar_semSalas_deveSalvarSemVincular() {
        ServicoCriacaoDto dto = dto(null);
        Mockito.when(servicoRepository.save(Mockito.any(Servico.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Servico resultado = service.criar(dto);

        Assertions.assertTrue(resultado.getSalas().isEmpty());
        Mockito.verifyNoInteractions(salaRepository);
    }

    @DisplayName("Unidade: ServicoService | Cenario: criar | Dados: com lista vazia | Verifica: deve salvar sem vincular")
    @Test
    void criar_comListaVazia_deveSalvarSemVincular() {
        ServicoCriacaoDto dto = dto(List.of());
        Mockito.when(servicoRepository.save(Mockito.any(Servico.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Servico resultado = service.criar(dto);

        Assertions.assertTrue(resultado.getSalas().isEmpty());
        Mockito.verifyNoInteractions(salaRepository);
    }

    @DisplayName("Unidade: ServicoService | Cenario: atualizar | Dados: dados preparados no arrange do teste | Verifica: deve atualizar todos os campos")
    @Test
    void atualizar_deveAtualizarTodosOsCampos() {
        Servico existente = servico(1L);
        List<Sala> salas = List.of(sala(3L, "Sala 3"));
        Mockito.when(servicoRepository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(salaRepository.findAllById(List.of(3L))).thenReturn(salas);
        Mockito.when(servicoRepository.save(existente)).thenReturn(existente);

        Servico resultado = service.atualizar(1L, dto(List.of(3L)));

        Assertions.assertEquals("Servico", resultado.getNome());
        Assertions.assertEquals(new BigDecimal("100.00"), resultado.getValor());
        Assertions.assertEquals("Descricao", resultado.getDescricao());
        Assertions.assertEquals(60, resultado.getTempoMedio());
        Assertions.assertEquals(salas, resultado.getSalas());
    }

    @DisplayName("Unidade: ServicoService | Cenario: atualizar | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void atualizar_deveLancar_quandoNaoExiste() {
        Mockito.when(servicoRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizar(1L, dto(List.of())));
    }

    @DisplayName("Unidade: ServicoService | Cenario: deletar | Dados: dados preparados no arrange do teste | Verifica: deve excluir")
    @Test
    void deletar_deveExcluir() {
        Mockito.when(servicoRepository.existsById(1L)).thenReturn(true);

        service.deletar(1L);

        Mockito.verify(servicoRepository).deleteById(1L);
    }

    @DisplayName("Unidade: ServicoService | Cenario: deletar | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void deletar_deveLancar_quandoNaoExiste() {
        Mockito.when(servicoRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.deletar(1L));
        Mockito.verify(servicoRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    private ServicoCriacaoDto dto(List<Long> salasIds) {
        ServicoCriacaoDto dto = new ServicoCriacaoDto();
        dto.setNome("Servico");
        dto.setValor(new BigDecimal("100.00"));
        dto.setDescricao("Descricao");
        dto.setTempoMedio(60);
        dto.setSalasIds(salasIds);
        return dto;
    }

    private Servico servico(Long id) {
        Servico servico = new Servico();
        servico.setId(id);
        servico.setNome("Antigo");
        servico.setValor(new BigDecimal("50.00"));
        servico.setDescricao("Antiga");
        servico.setTempoMedio(30);
        return servico;
    }

    private Sala sala(Long id, String descricao) {
        Sala sala = new Sala();
        sala.setId(id);
        sala.setDescricao(descricao);
        return sala;
    }
}
