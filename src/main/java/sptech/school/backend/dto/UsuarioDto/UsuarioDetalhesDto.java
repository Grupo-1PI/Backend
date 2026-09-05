package sptech.school.backend.dto.UsuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sptech.school.backend.entity.Usuario;
import java.util.Collection;

@Schema(name = "Usuario - Detalhes", description = "Dados detalhados do usuário para contexto de segurança e sessão")
public class UsuarioDetalhesDto implements UserDetails {

    @Schema(description = "Nome do usuário", example = "Fernanda Henckel")
    private final Usuario usuario;
    private final Collection<? extends GrantedAuthority> authorities;

    public UsuarioDetalhesDto(Usuario usuario) {
        this(usuario, java.util.List.of());
    }

    public UsuarioDetalhesDto(
            Usuario usuario,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.usuario = usuario;
        this.authorities = authorities;
    }

    @Schema(hidden = true)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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