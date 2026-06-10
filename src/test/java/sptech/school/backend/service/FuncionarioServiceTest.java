package sptech.school.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.backend.dto.EnderecoDto.EnderecoDto;
import sptech.school.backend.dto.FuncionarioDto.FuncionarioAtualizacaoDto;
import sptech.school.backend.dto.FuncionarioDto.FuncionarioCriacaoDto;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.entity.Endereco;
import sptech.school.backend.entity.Especialidade;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.CargoRepository;
import sptech.school.backend.repository.EnderecoRepository;
import sptech.school.backend.repository.EspecialidadeRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service unitario - FuncionarioService")
class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CargoRepository cargoRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private EspecialidadeRepository especialidadeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private FuncionarioService service;

    @DisplayName("Unidade: FuncionarioService | Cenario: criar | Dados: quando valido | Verifica: deve salvar funcionario")
    @Test
    void criar_deveSalvarFuncionario_quandoValido() {
        FuncionarioCriacaoDto dto = criacaoDto(List.of(1L, 1L, 2L));
        Cargo cargo = cargo(1L, "Acupunturista");
        List<Especialidade> especialidades = List.of(especialidade(1L), especialidade(2L));
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        Mockito.when(enderecoRepository.save(Mockito.any(Endereco.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(passwordEncoder.encode("123456")).thenReturn("hash");
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(especialidadeRepository.findAllById(List.of(1L, 2L))).thenReturn(especialidades);
        Mockito.when(funcionarioRepository.save(Mockito.any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Funcionario resultado = service.criar(dto);

        Assertions.assertEquals(cargo, resultado.getCargo());
        Assertions.assertEquals(especialidades, resultado.getEspecialidades());
        Assertions.assertEquals("hash", resultado.getUsuario().getSenha());
        Assertions.assertTrue(resultado.getUsuario().getAtivo());
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: criar | Dados: quando lista nula | Verifica: deve salvar funcionario sem especialidades")
    @Test
    void criar_deveSalvarFuncionarioSemEspecialidades_quandoListaNula() {
        FuncionarioCriacaoDto dto = criacaoDto(null);
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo(1L, "Cargo")));
        Mockito.when(enderecoRepository.save(Mockito.any(Endereco.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(passwordEncoder.encode("123456")).thenReturn("hash");
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(funcionarioRepository.save(Mockito.any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Funcionario resultado = service.criar(dto);

        Assertions.assertTrue(resultado.getEspecialidades().isEmpty());
        Mockito.verifyNoInteractions(especialidadeRepository);
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: criar | Dados: quando lista vazia | Verifica: deve salvar funcionario sem especialidades")
    @Test
    void criar_deveSalvarFuncionarioSemEspecialidades_quandoListaVazia() {
        FuncionarioCriacaoDto dto = criacaoDto(List.of());
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo(1L, "Cargo")));
        Mockito.when(enderecoRepository.save(Mockito.any(Endereco.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(passwordEncoder.encode("123456")).thenReturn("hash");
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(funcionarioRepository.save(Mockito.any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Funcionario resultado = service.criar(dto);

        Assertions.assertTrue(resultado.getEspecialidades().isEmpty());
        Mockito.verifyNoInteractions(especialidadeRepository);
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: criar | Dados: quando email ja existe | Verifica: deve lancar")
    @Test
    void criar_deveLancar_quandoEmailJaExiste() {
        FuncionarioCriacaoDto dto = criacaoDto(List.of());
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new Usuario()));

        Assertions.assertThrows(ResponseStatusException.class, () -> service.criar(dto));
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: criar | Dados: quando cargo nao existe | Verifica: deve lancar")
    @Test
    void criar_deveLancar_quandoCargoNaoExiste() {
        FuncionarioCriacaoDto dto = criacaoDto(List.of());
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(cargoRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.criar(dto));
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: criar | Dados: quando especialidade nao existe | Verifica: deve lancar")
    @Test
    void criar_deveLancar_quandoEspecialidadeNaoExiste() {
        FuncionarioCriacaoDto dto = criacaoDto(List.of(1L, 2L));
        Mockito.when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo(1L, "Cargo")));
        Mockito.when(enderecoRepository.save(Mockito.any(Endereco.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(passwordEncoder.encode("123456")).thenReturn("hash");
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(especialidadeRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(especialidade(1L)));

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.criar(dto));
        Mockito.verify(funcionarioRepository, Mockito.never()).save(Mockito.any());
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: listar | Dados: dados preparados no arrange do teste | Verifica: deve retornar lista")
    @Test
    void listar_deveRetornarLista() {
        List<Funcionario> funcionarios = List.of(funcionario(1L));
        Mockito.when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        List<Funcionario> resultado = service.listar();

        Assertions.assertEquals(funcionarios, resultado);
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: buscar por ID | Dados: quando existe | Verifica: deve retornar")
    @Test
    void buscarPorId_deveRetornar_quandoExiste() {
        Funcionario funcionario = funcionario(1L);
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));

        Funcionario resultado = service.buscarPorId(1L);

        Assertions.assertEquals(funcionario, resultado);
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: buscar por ID | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void buscarPorId_deveLancar_quandoNaoExiste() {
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(1L));
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: atualizar | Dados: dados preparados no arrange do teste | Verifica: deve atualizar cargo e especialidades")
    @Test
    void atualizar_deveAtualizarCargoEEspecialidades() {
        Funcionario funcionario = funcionario(1L);
        Cargo cargo = cargo(2L, "Novo");
        List<Especialidade> especialidades = List.of(especialidade(2L));
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        Mockito.when(cargoRepository.findById(2L)).thenReturn(Optional.of(cargo));
        Mockito.when(especialidadeRepository.findAllById(List.of(2L))).thenReturn(especialidades);
        Mockito.when(funcionarioRepository.save(funcionario)).thenReturn(funcionario);

        Funcionario resultado = service.atualizar(1L, atualizacaoDto(2L, List.of(2L)));

        Assertions.assertEquals(cargo, resultado.getCargo());
        Assertions.assertEquals(especialidades, resultado.getEspecialidades());
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: atualizar | Dados: quando campos nulos | Verifica: deve manter cargo e especialidades")
    @Test
    void atualizar_deveManterCargoEEspecialidades_quandoCamposNulos() {
        Funcionario funcionario = funcionario(1L);
        FuncionarioAtualizacaoDto dto = new FuncionarioAtualizacaoDto();
        dto.setCargoId(null);
        dto.setEspecialidadesIds(null);
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        Mockito.when(funcionarioRepository.save(funcionario)).thenReturn(funcionario);

        Funcionario resultado = service.atualizar(1L, dto);

        Assertions.assertEquals(funcionario.getCargo(), resultado.getCargo());
        Mockito.verifyNoInteractions(cargoRepository, especialidadeRepository);
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: atualizar | Dados: quando funcionario nao existe | Verifica: deve lancar")
    @Test
    void atualizar_deveLancar_quandoFuncionarioNaoExiste() {
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizar(1L, atualizacaoDto(1L, List.of())));
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: atualizar | Dados: quando cargo nao existe | Verifica: deve lancar")
    @Test
    void atualizar_deveLancar_quandoCargoNaoExiste() {
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario(1L)));
        Mockito.when(cargoRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizar(1L, atualizacaoDto(2L, List.of())));
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: deletar | Dados: dados preparados no arrange do teste | Verifica: deve deletar")
    @Test
    void deletar_deveDeletar() {
        Funcionario funcionario = funcionario(1L);
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));

        service.deletar(1L);

        Mockito.verify(funcionarioRepository).delete(funcionario);
    }

    @DisplayName("Unidade: FuncionarioService | Cenario: deletar | Dados: quando nao existe | Verifica: deve lancar")
    @Test
    void deletar_deveLancar_quandoNaoExiste() {
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> service.deletar(1L));
    }

    private FuncionarioCriacaoDto criacaoDto(List<Long> especialidadesIds) {
        FuncionarioCriacaoDto dto = new FuncionarioCriacaoDto();
        dto.setNome("Funcionario");
        dto.setEmail("funcionario@email.com");
        dto.setTelefone("11999990000");
        dto.setSenha("123456");
        dto.setDataNascimento(LocalDate.of(1990, 1, 1));
        dto.setEndereco(enderecoDto());
        dto.setCargoId(1L);
        dto.setEspecialidadesIds(especialidadesIds);
        return dto;
    }

    private FuncionarioAtualizacaoDto atualizacaoDto(Long cargoId, List<Long> especialidadesIds) {
        FuncionarioAtualizacaoDto dto = new FuncionarioAtualizacaoDto();
        dto.setCargoId(cargoId);
        dto.setEspecialidadesIds(especialidadesIds);
        return dto;
    }

    private EnderecoDto enderecoDto() {
        EnderecoDto dto = new EnderecoDto();
        dto.setCep("01001-000");
        dto.setLogradouro("Rua A");
        dto.setBairro("Centro");
        dto.setCidade("Sao Paulo");
        dto.setUf("SP");
        dto.setNumero("100");
        dto.setComplemento("Apto 1");
        return dto;
    }

    private Funcionario funcionario(Long id) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(id);
        funcionario.setCargo(cargo(1L, "Cargo"));
        funcionario.setEspecialidades(List.of(especialidade(1L)));
        return funcionario;
    }

    private Cargo cargo(Long id, String nome) {
        Cargo cargo = new Cargo();
        cargo.setId(id);
        cargo.setNome(nome);
        return cargo;
    }

    private Especialidade especialidade(Long id) {
        Especialidade especialidade = new Especialidade();
        especialidade.setId(id);
        return especialidade;
    }
}
