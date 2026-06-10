package sptech.school.backend.mapper;

import sptech.school.backend.dto.ServicoDto.ServicoResponseDto;
import sptech.school.backend.dto.ServicoDto.ServicoResumoDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Servico;

public class ServicoMapper {

    public static ServicoResumoDto toResumo(Servico servico) {
        ServicoResumoDto dto = new ServicoResumoDto();

        dto.setId(servico.getId());
        dto.setNome(servico.getNome());

        return dto;
    }

    public static ServicoResponseDto toResponse(Servico servico) {
        ServicoResponseDto dto = new ServicoResponseDto();
        dto.setId(servico.getId());
        dto.setNome(servico.getNome());
        dto.setValor(servico.getValor());
        dto.setDescricao(servico.getDescricao());
        dto.setTempoMedio(servico.getTempoMedio());
        dto.setSalas(
                servico.getSalas().stream()
                        .map(Sala::getDescricao)
                        .toList()
        );
        return dto;
    }
}
