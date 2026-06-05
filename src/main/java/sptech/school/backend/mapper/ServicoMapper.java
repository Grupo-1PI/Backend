package sptech.school.backend.mapper;

import sptech.school.backend.dto.ServicoDto.ServicoResumoDto;
import sptech.school.backend.entity.Servico;

public class ServicoMapper {

    public static ServicoResumoDto toResumo(Servico servico) {
        ServicoResumoDto dto = new ServicoResumoDto();

        dto.setId(servico.getId());
        dto.setNome(servico.getNome());

        return dto;
    }
}
