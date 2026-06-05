package sptech.school.backend.dto.UsuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sptech.school.backend.dto.EnderecoDto.EnderecoDto;

import java.time.LocalDate;

@Schema(name = "Usuario - Criação", description = "DTO para criação de usuário")
public class UsuarioCriacaoDto {

    @NotBlank
    @Size(min = 3, max = 120)
    @Schema(description = "Nome completo do usuário", example = "Felipe Silva",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Schema(description = "Número de telefone do usuário", example = "11999999999")
    private String telefone;

    @Email
    @NotBlank
    @Schema(description = "Email do usuário", example = "felipe@email.com")
    private String email;

    @NotBlank
    @Size(min = 6, max = 255)
    @Schema(description = "Senha do usuário", example = "123456")
    private String senha;

    @NotNull
    @Schema(description = "Data de nascimento do usuário", example = "2000-05-10")
    private LocalDate dataNascimento;

    @Schema(description = "Endereco completo do usuario")
    @Valid
    @NotNull
    private EnderecoDto endereco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
