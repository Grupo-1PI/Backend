package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.backend.dto.EspecialidadeDto.EspecialidadeCriacaoDto;
import sptech.school.backend.entity.Especialidade;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.EspecialidadeRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.ServicoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - EspecialidadeService")
class EspecialidadeServiceTest {

    @Mock
    private EspecialidadeRepository repository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private EspecialidadeService service;

    @DisplayName("Unidade: EspecialidadeService | Cenario: listar | Dados: dados preparados no arrange do teste | Verifica: deve retornar lista")
    @Test
    void listar_deveRetornarLista() {
        List<Especialidade> especialidades = List.of(especialidade(1L, "Acupuntura"));
        Mockito.when(repository.findAll()).thenReturn(especialidades);

        List<Especialidade> resultado = service.listar();

        Assertions.assertEquals(especialidades, resultado);
    }

    @DisplayName("Unidade: EspecialidadeService | Cenario: buscar por ID | Dados: quando existe | Verifica: deve retornar")
    @Test
    void buscarPorId_deveRetornar_quandoExiste() {
        Especialidade especialidade = especialidade(1L, "Acupuntura");
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(especialidade));

        Especialidade resultado = service.buscarPorId(1L);

        Assertions.assertEquals(especialidade, resultado);
    }

    @DisplayName("Unidade: EspecialidadeService | Cenario: buscar por ID | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void buscarPorId_deveLancar_quandoNaoExiste() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(1L));
    }

    @DisplayName("Unidade: EspecialidadeService | Cenario: criar | Dados: dados preparados no arrange do teste | Verifica: deve salvar")
    @Test
    void criar_deveSalvar() {
        EspecialidadeCriacaoDto dto = dto("Acupuntura", List.of(1L, 1L, 2L));
        List<Servico> servicos = List.of(servico(1L, "Sessao"), servico(2L, "Auriculo"));
        Mockito.when(servicoRepository.findAllById(List.of(1L, 2L))).thenReturn(servicos);
        Mockito.when(repository.save(Mockito.any(Especialidade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Especialidade resultado = service.criar(dto);

        Assertions.assertEquals("Acupuntura", resultado.getNome());
        Assertions.assertEquals(servicos, resultado.getServicos());
    }

    @DisplayName("Unidade: EspecialidadeService | Cenario: criar | Dados: sem servicos | Verifica: deve salvar lista vazia")
    @Test
    void criar_semServicos_deveSalvarListaVazia() {
        EspecialidadeCriacaoDto dto = dto("Acupuntura", null);
        Mockito.when(repository.save(Mockito.any(Especialidade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Especialidade resultado = service.criar(dto);

        Assertions.assertTrue(resultado.getServicos().isEmpty());
        Mockito.verifyNoInteractions(servicoRepository);
    }

    @DisplayName("Unidade: EspecialidadeService | Cenario: criar | Dados: com lista vazia | Verifica: deve salvar lista vazia")
    @Test
    void criar_comListaVazia_deveSalvarListaVazia() {
        EspecialidadeCriacaoDto dto = dto("Acupuntura", List.of());
        Mockito.when(repository.save(Mockito.any(Especialidade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Especialidade resultado = service.criar(dto);

        Assertions.assertTrue(resultado.getServicos().isEmpty());
        Mockito.verifyNoInteractions(servicoRepository);
    }

    @DisplayName("Unidade: EspecialidadeService | Cenario: criar | Dados: quando servico nao existe | Verifica: deve lancar")
    @Test
    void criar_deveLancar_quandoServicoNaoExiste() {
        EspecialidadeCriacaoDto dto = dto("Acupuntura", List.of(1L, 2L));
        Mockito.when(servicoRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(servico(1L, "Sessao")));

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.criar(dto));
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @DisplayName("Unidade: EspecialidadeService | Cenario: atualizar | Dados: dados preparados no arrange do teste | Verifica: deve atualizar")
    @Test
    void atualizar_deveAtualizar() {
        Especialidade existente = especialidade(1L, "Antiga");
        List<Servico> servicos = List.of(servico(2L, "Auriculo"));
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(servicoRepository.findAllById(List.of(2L))).thenReturn(servicos);
        Mockito.when(repository.save(existente)).thenReturn(existente);

        Especialidade resultado = service.atualizar(1L, dto("Nova", List.of(2L)));

        Assertions.assertEquals("Nova", resultado.getNome());
        Assertions.assertEquals(servicos, resultado.getServicos());
    }

    @DisplayName("Unidade: EspecialidadeService | Cenario: atualizar | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void atualizar_deveLancar_quandoNaoExiste() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.atualizar(1L, dto("Nova", List.of(1L)))
        );
    }

    @DisplayName("Unidade: EspecialidadeService | Cenario: deletar | Dados: dados preparados no arrange do teste | Verifica: deve deletar")
    @Test
    void deletar_deveDeletar() {
        Especialidade especialidade = especialidade(1L, "Acupuntura");
        especialidade.setServicos(List.of(servico(1L, "Sessao")));
        Funcionario funcionario = new Funcionario();
        funcionario.setEspecialidades(new ArrayList<>(List.of(especialidade)));
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(especialidade));
        Mockito.when(funcionarioRepository.findByEspecialidadesId(1L)).thenReturn(List.of(funcionario));

        service.deletar(1L);

        Assertions.assertTrue(funcionario.getEspecialidades().isEmpty());
        Assertions.assertTrue(especialidade.getServicos().isEmpty());
        Mockito.verify(repository).delete(especialidade);
    }

    @DisplayName("Unidade: EspecialidadeService | Cenario: deletar | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void deletar_deveLancar_quandoNaoExiste() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.deletar(1L));
        Mockito.verify(repository, Mockito.never()).delete(Mockito.any());
    }

    private EspecialidadeCriacaoDto dto(String nome, List<Long> servicosIds) {
        EspecialidadeCriacaoDto dto = new EspecialidadeCriacaoDto();
        dto.setNome(nome);
        dto.setServicosIds(servicosIds);
        return dto;
    }

    private Especialidade especialidade(Long id, String nome) {
        Especialidade especialidade = new Especialidade();
        especialidade.setId(id);
        especialidade.setNome(nome);
        return especialidade;
    }

    private Servico servico(Long id, String nome) {
        Servico servico = new Servico();
        servico.setId(id);
        servico.setNome(nome);
        return servico;
    }
}
