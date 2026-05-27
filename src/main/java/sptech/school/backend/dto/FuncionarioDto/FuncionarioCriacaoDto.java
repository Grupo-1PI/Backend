package sptech.school.backend.dto.FuncionarioDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import sptech.school.backend.dto.EnderecoDto.EnderecoDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "DTO para criação de funcionário")
public class FuncionarioCriacaoDto {

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String telefone;

    @NotBlank
    private String senha;

    @NotNull
    private LocalDate dataNascimento;

    @Valid
    @NotNull
    private EnderecoDto endereco;

    @Schema(example = "1")
    private Long cargoId;

    private List<Long> especialidadesIds = new ArrayList<>();

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    public List<Long> getEspecialidadesIds() {
        return especialidadesIds;
    }

    public void setEspecialidadesIds(List<Long> especialidadesIds) {
        this.especialidadesIds = especialidadesIds;
    }
}
