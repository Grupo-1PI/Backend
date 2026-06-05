package sptech.school.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EspecialidadeServiceTest {

    @Mock
    private EspecialidadeRepository repository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private EspecialidadeService service;

    @Test
    @DisplayName("Deve criar especialidade com servicos")
    void deveCriarEspecialidadeComServicos() {
        EspecialidadeCriacaoDto dto = criarDto("Acupuntura", List.of(1L, 1L, 2L));
        List<Servico> servicos = List.of(criarServico(1L, "Acupuntura Sistemica"), criarServico(2L, "Auriculoterapia"));

        Mockito.when(servicoRepository.findAllById(List.of(1L, 2L))).thenReturn(servicos);
        Mockito.when(repository.save(Mockito.any(Especialidade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Especialidade resultado = service.criar(dto);

        Assertions.assertEquals("Acupuntura", resultado.getNome());
        Assertions.assertEquals(servicos, resultado.getServicos());
        Mockito.verify(servicoRepository).findAllById(List.of(1L, 2L));
        Mockito.verify(repository).save(Mockito.any(Especialidade.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando algum servico nao existir")
    void deveLancarExceptionQuandoServicoNaoExistir() {
        EspecialidadeCriacaoDto dto = criarDto("Acupuntura", List.of(1L, 2L));

        Mockito.when(servicoRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(criarServico(1L, "Acupuntura Sistemica")));

        Assertions.assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.criar(dto)
        );

        Mockito.verify(servicoRepository).findAllById(List.of(1L, 2L));
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Especialidade.class));
    }

    @Test
    @DisplayName("Deve deletar especialidade removendo vinculos com funcionarios")
    void deveDeletarEspecialidadeRemovendoVinculosComFuncionarios() {
        Especialidade especialidade = criarEspecialidade(1L, "Acupuntura");
        especialidade.setServicos(List.of(criarServico(1L, "Acupuntura Sistemica")));
        Funcionario funcionario = new Funcionario();
        funcionario.setEspecialidades(new java.util.ArrayList<>(List.of(especialidade)));

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(especialidade));
        Mockito.when(funcionarioRepository.findByEspecialidadesId(1L)).thenReturn(List.of(funcionario));

        service.deletar(1L);

        Assertions.assertTrue(funcionario.getEspecialidades().isEmpty());
        Assertions.assertTrue(especialidade.getServicos().isEmpty());
        Mockito.verify(repository).delete(especialidade);
    }

    private EspecialidadeCriacaoDto criarDto(String nome, List<Long> servicosIds) {
        EspecialidadeCriacaoDto dto = new EspecialidadeCriacaoDto();
        dto.setNome(nome);
        dto.setServicosIds(servicosIds);
        return dto;
    }

    private Especialidade criarEspecialidade(Long id, String nome) {
        Especialidade especialidade = new Especialidade();
        especialidade.setId(id);
        especialidade.setNome(nome);
        return especialidade;
    }

    private Servico criarServico(Long id, String nome) {
        Servico servico = new Servico();
        servico.setId(id);
        servico.setNome(nome);
        return servico;
    }
}
