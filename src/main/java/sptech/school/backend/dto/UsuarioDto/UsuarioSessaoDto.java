package sptech.school.backend.dto.UsuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Usuário - Sessão",
        description = "Dados de perfil do usuário autenticado. O token de acesso não é incluído aqui por segurança, sendo enviado via Cookie HttpOnly."
)
public class UsuarioSessaoDto {

    @Schema(description = "Identificador único do usuário logado", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Nome do usuário para exibição na interface", example = "Fernanda Henckel")
    private String nome;

    @Schema(description = "E-mail do usuário logado", example = "fernanda.henckel@gmail.com")
    private String email;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}