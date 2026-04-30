package sptech.school.backend.dto.UsuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para login")
public class UsuarioLoginDto {

    @Schema(example = "felipe@email.com")
    private String email;

    @Schema(example = "123456")
    private String senha;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}