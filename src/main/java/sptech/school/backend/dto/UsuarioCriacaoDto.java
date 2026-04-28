package sptech.school.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioCriacaoDto {

    @Size(min = 3, max = 120)
    @Schema(description = "Nome do usuário", example = "Felipe")
    private String nome;

    @Email
    @Schema(description = "Email do usuário", example = "felipe@email.com")
    private String email;

    @Size(min = 6, max = 255)
    @Schema(description = "Senha do usuário", example = "123456")
    private String senha;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}