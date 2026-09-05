package sptech.school.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.repository.UsuarioRepository;
import sptech.school.backend.repository.FuncionarioRepository;
import java.util.Collections;
import sptech.school.backend.dto.UsuarioDto.UsuarioDetalhesDto;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository, FuncionarioRepository funcionarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado")
                );

        var authorities = funcionarioRepository.findByUsuarioId(usuario.getId())
            .map(funcionario -> funcionario.getCargo().getPermissoes().stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao.getNome()))
                .toList())
            .orElseGet(() -> Collections.singletonList(new SimpleGrantedAuthority("CLIENTE")));

        return new UsuarioDetalhesDto(usuario, authorities);
    }
}