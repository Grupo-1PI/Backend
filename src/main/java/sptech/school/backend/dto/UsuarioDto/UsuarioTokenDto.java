package sptech.school.backend.dto.UsuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Usuário - Token", description = "Objeto de retorno contendo os dados do usuário e o token JWT gerado")
public class UsuarioTokenDto {

    @Schema(description = "Identificador único do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Nome do usuário autenticado", example = "Fernanda Henckel")
    private String nome;

    @Schema(description = "E-mail do usuário autenticado", example = "fernanda.henckel@gmail.com")
    private String email;

    @Schema(description = "Token JWT de autenticação (Bearer)", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmZWxpcGVAZW1haWwuY29tIiwiaWF0Ijo...")
    private String token;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}