package sptech.school.backend.mapper;

import sptech.school.backend.dto.FuncionarioDto.FuncionarioResponseDto;
import sptech.school.backend.entity.Especialidade;
import sptech.school.backend.entity.Funcionario;
import java.util.Comparator;
import java.util.Objects;

public class FuncionarioMapper {

    public static FuncionarioResponseDto toResponse(Funcionario f) {

        FuncionarioResponseDto dto = new FuncionarioResponseDto();

        dto.setId(f.getId());
        dto.setNome(f.getUsuario().getNome());
        dto.setEmail(f.getUsuario().getEmail());
        dto.setTelefone(f.getUsuario().getTelefone());
        dto.setCargo(f.getCargo().getNome());
        dto.setEspecialidades(
                f.getEspecialidades()
                        .stream()
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparing(Especialidade::getNome))
                        .map(Especialidade::getNome)
                        .toList()
        );

        return dto;
    }

    public static FuncionarioResponseDto toDto(Funcionario f) {
        return toResponse(f);
    }
}
