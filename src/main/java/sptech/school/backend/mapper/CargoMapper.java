package sptech.school.backend.mapper;

import sptech.school.backend.dto.CargoDto.CargoResponseDto;
import sptech.school.backend.dto.CargoDto.PermissaoResumoDto;
import sptech.school.backend.entity.Cargo;
import sptech.school.backend.entity.Funcionario;
import sptech.school.backend.entity.Permissao;
import java.util.Collections;
import java.util.List;

public class CargoMapper {

    public static CargoResponseDto toResponse(Cargo cargo) {
        return toDto(cargo);
    }

    public static CargoResponseDto toDto(Cargo cargo) {

        CargoResponseDto dto = new CargoResponseDto();

        dto.setId(cargo.getId());
        dto.setNome(cargo.getNome());
        dto.setUsuarios(toUsuarios(cargo.getFuncionarios()));
        dto.setPermissoes(toPermissoes(cargo.getPermissoes()));

        return dto;
    }

    private static List<String> toUsuarios(List<Funcionario> funcionarios) {
        if (funcionarios == null) {
            return Collections.emptyList();
        }

        return funcionarios.stream()
                .map(Funcionario::getUsuario)
                .map(usuario -> usuario.getNome())
                .toList();
    }

    private static List<PermissaoResumoDto> toPermissoes(List<Permissao> permissoes) {
        if (permissoes == null) {
            return Collections.emptyList();
        }

        return permissoes.stream()
                .map(CargoMapper::toPermissaoDto)
                .toList();
    }

    private static PermissaoResumoDto toPermissaoDto(Permissao permissao) {
        PermissaoResumoDto dto = new PermissaoResumoDto();

        dto.setId(permissao.getId());
        dto.setNome(permissao.getNome());

        return dto;
    }
}
