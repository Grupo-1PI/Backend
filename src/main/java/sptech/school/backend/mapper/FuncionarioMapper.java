package sptech.school.backend.mapper;

import sptech.school.backend.dto.FuncionarioDto.FuncionarioResponseDto;
import sptech.school.backend.entity.Funcionario;

public class FuncionarioMapper {

    public static FuncionarioResponseDto toDto(Funcionario f) {

        FuncionarioResponseDto dto = new FuncionarioResponseDto();

        dto.setId(f.getId());
        dto.setNomeUsuario(f.getUsuario().getNome());
        dto.setCargo(f.getCargo().getNome());

        return dto;
    }
}