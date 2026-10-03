package sptech.school.backend.dto.FuncionarioDto;

import sptech.school.backend.dto.EnderecoDto.EnderecoDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Campos aceitos na atualizacao de um funcionario.
 *
 * Todos sao opcionais: o que vier nulo nao e alterado. O endereco
 * acompanha os mesmos campos usados na criacao.
 */
public class FuncionarioAtualizacaoDto {

    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private EnderecoDto endereco;
    private Long cargoId;
    private List<Long> especialidadesIds = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public EnderecoDto getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDto endereco) {
        this.endereco = endereco;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public List<Long> getEspecialidadesIds() {
        return especialidadesIds;
    }

    public void setEspecialidadesIds(List<Long> especialidadesIds) {
        this.especialidadesIds = especialidadesIds;
    }
}