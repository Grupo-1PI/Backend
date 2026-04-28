package sptech.school.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.repository.ClienteRepository;
import sptech.school.backend.dto.UsuarioDetalhesDto;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    private ClienteRepository usuarioRepository;

    public AutenticacaoService(ClienteRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Método da interface implementada
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Cliente> usuarioOpt = usuarioRepository.findByEmail(username);

        if (usuarioOpt.isEmpty()) {

            throw new UsernameNotFoundException(String.format("usuário: %s não encontrado", username));
        }

        return new UsuarioDetalhesDto(usuarioOpt.get());
    }
}
