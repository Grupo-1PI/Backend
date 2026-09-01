package sptech.school.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.repository.FuncionarioRepository;
import sptech.school.backend.repository.UsuarioRepository;
import sptech.school.backend.dto.UsuarioDto.UsuarioDetalhesDto;
import java.util.List;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository, FuncionarioRepository funcionarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {

        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado")
                );

        List<String> permissoes = funcionarioRepository.findByUsuarioId(usuario.getId())
                .map(funcionario -> funcionario.getCargo().getPermissoes().stream()
                        .map(permissao -> permissao.getNome())
                        .toList())
                .orElseGet(List::of);

        return new UsuarioDetalhesDto(usuario, permissoes);
    }
}
