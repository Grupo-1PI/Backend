package sptech.school.backend.dto.UsuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Usuário - Listar", description = "DTO para listagem de usuários")
public class UsuarioListarDto {

    @Schema(description = "Id do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nome do usuário", example = "Fernanda Henckel")
    private String nome;

    @Schema(description = "Email do usuário", example = "fernanda.henckel@gmail.com")
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}