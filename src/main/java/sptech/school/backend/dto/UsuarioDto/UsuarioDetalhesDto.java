package sptech.school.backend.dto.UsuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sptech.school.backend.entity.Usuario;
import java.util.Collection;
import java.util.Collections;

@Schema(name = "Usuario - Detalhes", description = "Dados detalhados do usuário para contexto de segurança e sessão")
public class UsuarioDetalhesDto implements UserDetails {

    @Schema(description = "Nome do usuário", example = "Fernanda Henckel")
    private final Usuario usuario;

    public UsuarioDetalhesDto(Usuario usuario) {
        this.usuario = usuario;
    }

    @Schema(hidden = true)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Schema(hidden = true)
    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Schema(description = "E-mail de autenticação")
    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Schema(hidden = true)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Schema(hidden = true)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Schema(hidden = true)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Schema(hidden = true)
    @Override
    public boolean isEnabled() {
        return usuario.getAtivo();
    }

    public Usuario getUsuario() {
        return usuario;
    }
}