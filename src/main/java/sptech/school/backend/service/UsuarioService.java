package sptech.school.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.backend.config.GerenciadorTokenJwt;
import sptech.school.backend.dto.UsuarioDto.UsuarioCriacaoDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioLoginDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioTokenDto;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.entity.Endereco;
import sptech.school.backend.mapper.UsuarioMapper;
import sptech.school.backend.repository.EnderecoRepository;
import sptech.school.backend.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private static final long ENDERECO_PADRAO_ID = 1L;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void criar(UsuarioCriacaoDto dto) {

        Usuario usuario = UsuarioMapper.toEntity(dto);

        Long enderecoId = dto.getEnderecoId() != null ? dto.getEnderecoId() : ENDERECO_PADRAO_ID;

        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Endereço não encontrado")
                );

        usuario.setEndereco(endereco);

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        usuarioRepository.save(usuario);
    }

    public UsuarioTokenDto login(UsuarioLoginDto dto) {

        UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getSenha()
                );

        Authentication authentication = authenticationManager.authenticate(credentials);

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.toTokenDto(usuario, token);
    }
}