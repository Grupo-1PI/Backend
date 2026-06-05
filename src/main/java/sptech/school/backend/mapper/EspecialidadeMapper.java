package sptech.school.backend.mapper;

import sptech.school.backend.dto.EspecialidadeDto.EspecialidadeResponseDto;
import sptech.school.backend.dto.ServicoDto.ServicoResumoDto;
import sptech.school.backend.entity.Especialidade;
import java.util.Collections;
import java.util.List;

public class EspecialidadeMapper {

    public static EspecialidadeResponseDto toResponse(Especialidade especialidade) {
        EspecialidadeResponseDto dto = new EspecialidadeResponseDto();

        dto.setId(especialidade.getId());
        dto.setNome(especialidade.getNome());
        dto.setServicos(toServicos(especialidade));

        return dto;
    }

    private static List<ServicoResumoDto> toServicos(Especialidade especialidade) {
        if (especialidade.getServicos() == null) {
            return Collections.emptyList();
        }

        return especialidade.getServicos()
                .stream()
                .map(ServicoMapper::toResumo)
                .toList();
    }
}
