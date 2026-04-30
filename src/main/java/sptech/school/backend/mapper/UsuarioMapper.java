package sptech.school.backend.mapper;

import sptech.school.backend.dto.UsuarioDto.*;
import sptech.school.backend.entity.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioCriacaoDto dto) {

        Usuario usuario = new Usuario();

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setTelefone(dto.getTelefone());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setAtivo(true);

        return usuario;
    }

    public static Usuario toLoginEntity(UsuarioLoginDto dto) {

        Usuario usuario = new Usuario();

        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        return usuario;
    }

    public static UsuarioTokenDto toTokenDto(Usuario usuario, String token) {

        UsuarioTokenDto dto = new UsuarioTokenDto();

        dto.setUserId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setToken(token);

        return dto;
    }

    public static UsuarioSessaoDto toSessaoDto(UsuarioTokenDto tokenDto) {

        UsuarioSessaoDto dto = new UsuarioSessaoDto();

        dto.setUserId(tokenDto.getUserId());
        dto.setNome(tokenDto.getNome());
        dto.setEmail(tokenDto.getEmail());

        return dto;
    }

    public static UsuarioListarDto toListDto(Usuario usuario) {

        UsuarioListarDto dto = new UsuarioListarDto();

        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());

        return dto;
    }
}