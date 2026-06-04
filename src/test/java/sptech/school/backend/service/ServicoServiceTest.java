package sptech.school.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.dto.ServicoDto.ServicoRequestDto;
import sptech.school.backend.dto.ServicoDto.ServicoResponseDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.mapper.ServicoMapper;
import sptech.school.backend.repository.SalaRepository;
import sptech.school.backend.repository.ServicoRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ServicoServiceTest {

    @Mock
    private ServicoRepository repository;

    @Mock
    private ServicoMapper mapper;

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private ServicoService service;

    @Test
    @DisplayName("Deve lançar exceção quando não há serviços cadastrados")
    void deveLancarExcecaoQuandoNaoHaServicos() {
        // Given
        var listaVazia = Collections.EMPTY_LIST;

        // When
        Mockito.when(repository.findAll()).thenReturn(listaVazia);

        // Then
        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.listar()
        );
    }

    @Test
    @DisplayName("Deve retornar lista com serviços quando há dados cadastrados")
    void deveRetornarListaComServicosQuandoHaDados() {
        // Given
        Sala sala = new Sala();
        sala.setId(1L);

        Servico servico = new Servico(
                1L, "Acupuntura", null,
                "Sessão padrão", 60, List.of(sala)
        );

        ServicoResponseDto responseDto = new ServicoResponseDto(
                1L, "Acupuntura", null,
                "Sessão padrão", 60, List.of(sala)
        );

        // When
        Mockito.when(repository.findAll()).thenReturn(List.of(servico));
        Mockito.when(mapper.toResponseList(List.of(servico))).thenReturn(List.of(responseDto));

        // Then
        List<ServicoResponseDto> resultado = service.listar();

        Assertions.assertFalse(resultado.isEmpty());
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Acupuntura", resultado.get(0).getNome());
    }

    @Test
    @DisplayName("Deve salvar serviço corretamente")
    void deveSalvarServicoCorretamente() {
        // Given
        Sala sala = new Sala();
        sala.setId(1L);

        ServicoRequestDto requestDto = new ServicoRequestDto(
                "Acupuntura", null,
                "Sessão padrão", 60, List.of(1L)
        );

        Servico servico = new Servico(
                null, "Acupuntura", null,
                "Sessão padrão", 60, List.of(sala)
        );

        // When
        Mockito.when(mapper.toEntity(requestDto)).thenReturn(servico);

        // Then
        service.salvar(requestDto);

        Mockito.verify(repository, Mockito.times(1)).save(servico);
    }

    @Test
    @DisplayName("Deve atualizar todos os campos do serviço corretamente")
    void deveAtualizarEntidadeCorretamente() {
        // Given
        Sala sala = new Sala();
        sala.setId(1L);

        Servico servicoExistente = new Servico(
                1L, "Acupuntura", null,
                "Sessão padrão", 60, List.of(sala)
        );

        ServicoRequestDto requestDto = new ServicoRequestDto(
                "Massagem", null,
                "Nova descrição", 90, List.of(1L)
        );

        // When
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(servicoExistente));

        // Then
        service.atualizarEntidade(1L, requestDto);

        Assertions.assertEquals("Massagem", servicoExistente.getNome());
        Assertions.assertEquals("Nova descrição", servicoExistente.getDescricao());
        Assertions.assertEquals(90, servicoExistente.getTempoMedio());
        Mockito.verify(repository, Mockito.times(1)).save(servicoExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar entidade com ID inválido")
    void deveLancarExcecaoAoAtualizarEntidadeComIdInvalido() {
        // Given
        ServicoRequestDto requestDto = new ServicoRequestDto(
                "Massagem", null,
                "Nova descrição", 90, List.of(1L)
        );

        // When
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.atualizarEntidade(99L, requestDto)
        );

        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Deve atualizar parcialmente apenas os campos não nulos")
    void deveAtualizarParcialmenteApenasOsCamposNaoNulos() {
        // Given
        Sala sala = new Sala();
        sala.setId(1L);

        Servico servicoExistente = new Servico(
                1L, "Acupuntura", null,
                "Sessão padrão", 60, List.of(sala)
        );

        ServicoRequestDto requestDto = new ServicoRequestDto(
                "Reiki", null,
                null, null, null
        );

        // When
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(servicoExistente));

        // Then
        service.atualizar(1L, requestDto);

        Assertions.assertEquals("Reiki", servicoExistente.getNome());
        Assertions.assertEquals("Sessão padrão", servicoExistente.getDescricao());
        Assertions.assertEquals(60, servicoExistente.getTempoMedio());
        Mockito.verify(salaRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).save(servicoExistente);
    }

    @Test
    @DisplayName("Deve atualizar salas quando salasIds é informado no PATCH")
    void deveAtualizarSalasQuandoSalasIdsInformado() {
        // Given
        Sala salaAntiga = new Sala();
        salaAntiga.setId(1L);

        Servico servicoExistente = new Servico(
                1L, "Acupuntura", null,
                "Sessão padrão", 60, List.of(salaAntiga)
        );

        Sala novaSala = new Sala();
        novaSala.setId(2L);

        ServicoRequestDto requestDto = new ServicoRequestDto(
                null, null, null, null, List.of(2L)
        );

        // When
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(servicoExistente));
        Mockito.when(salaRepository.findById(2L)).thenReturn(Optional.of(novaSala));

        // Then
        service.atualizar(1L, requestDto);

        Assertions.assertEquals(1, servicoExistente.getSalas().size());
        Assertions.assertEquals(2L, servicoExistente.getSalas().get(0).getId());
        Mockito.verify(salaRepository, Mockito.times(1)).findById(2L);
        Mockito.verify(repository, Mockito.times(1)).save(servicoExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar parcialmente com ID inválido")
    void deveLancarExcecaoAoAtualizarParcialmenteComIdInvalido() {
        // Given
        ServicoRequestDto requestDto = new ServicoRequestDto(
                "Reiki", null, null, null, null
        );

        // When
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.atualizar(99L, requestDto)
        );

        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Deve deletar serviço corretamente quando ID é válido")
    void deveDeletarServicoCorretamente() {
        // Given
        Sala sala = new Sala();
        sala.setId(1L);

        Servico servico = new Servico(
                1L, "Acupuntura", null,
                "Sessão padrão", 60, List.of(sala)
        );

        // When
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(servico));

        // Then
        service.deletar(1L);

        Mockito.verify(repository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar com ID inválido")
    void deveLancarExcecaoAoDeletarComIdInvalido() {
        // Given
        Optional<Servico> empty = Optional.empty();

        // When
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(empty);

        // Then
        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.deletar(99L)
        );

        Mockito.verify(repository, Mockito.never()).deleteById(Mockito.any());
    }
}