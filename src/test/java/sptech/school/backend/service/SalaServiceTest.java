package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.dto.SalaDto.SalaCriacaoDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.SalaRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - SalaService")
class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SalaService service;

    @DisplayName("Unidade: SalaService | Cenario: listar | Dados: dados preparados no arrange do teste | Verifica: deve retornar lista")
    @Test
    void listar_deveRetornarLista() {
        List<Sala> salas = List.of(sala(1L, "Sala 1"));
        Mockito.when(salaRepository.findAll()).thenReturn(salas);

        List<Sala> resultado = service.listar();

        Assertions.assertEquals(salas, resultado);
    }

    @DisplayName("Unidade: SalaService | Cenario: buscar por ID | Dados: quando existe | Verifica: deve retornar sala")
    @Test
    void buscarPorId_deveRetornarSala_quandoExiste() {
        Sala sala = sala(1L, "Sala 1");
        Mockito.when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));

        Sala resultado = service.buscarPorId(1L);

        Assertions.assertEquals(sala, resultado);
    }

    @DisplayName("Unidade: SalaService | Cenario: buscar por ID | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void buscarPorId_deveLancar_quandoNaoExiste() {
        Mockito.when(salaRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(1L));
    }

    @DisplayName("Unidade: SalaService | Cenario: criar | Dados: dados preparados no arrange do teste | Verifica: deve salvar e salvar")
    @Test
    void criar_deveSalvarESalvar() {
        SalaCriacaoDto dto = new SalaCriacaoDto("Sala Nova");
        Mockito.when(salaRepository.save(Mockito.any(Sala.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sala resultado = service.criar(dto);

        Assertions.assertEquals("Sala Nova", resultado.getDescricao());
        Mockito.verify(salaRepository).save(Mockito.any(Sala.class));
    }

    @DisplayName("Unidade: SalaService | Cenario: atualizar | Dados: dados preparados no arrange do teste | Verifica: deve atualizar descricao")
    @Test
    void atualizar_deveAtualizarDescricao() {
        Sala existente = sala(1L, "Antiga");
        SalaCriacaoDto dto = new SalaCriacaoDto("Nova");
        Mockito.when(salaRepository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(salaRepository.save(existente)).thenReturn(existente);

        Sala resultado = service.atualizar(1L, dto);

        Assertions.assertEquals("Nova", resultado.getDescricao());
    }

    @DisplayName("Unidade: SalaService | Cenario: atualizar | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void atualizar_deveLancar_quandoNaoExiste() {
        Mockito.when(salaRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.atualizar(1L, new SalaCriacaoDto("Nova"))
        );
    }

    @DisplayName("Unidade: SalaService | Cenario: deletar | Dados: dados preparados no arrange do teste | Verifica: deve deletar sala")
    @Test
    void deletar_deveDeletarSala() {
        Sala sala = sala(1L, "Sala 1");
        Mockito.when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));

        service.deletar(1L);

        Mockito.verify(salaRepository).delete(sala);
    }

    @DisplayName("Unidade: SalaService | Cenario: deletar | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void deletar_deveLancar_quandoNaoExiste() {
        Mockito.when(salaRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.deletar(1L));
        Mockito.verify(salaRepository, Mockito.never()).delete(Mockito.any());
    }

    private Sala sala(Long id, String descricao) {
        Sala sala = new Sala();
        sala.setId(id);
        sala.setDescricao(descricao);
        return sala;
    }
}
