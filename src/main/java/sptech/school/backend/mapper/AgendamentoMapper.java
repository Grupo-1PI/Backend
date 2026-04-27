package sptech.school.backend.mapper;

import sptech.school.backend.dto.AgendamentoResponseDto;
import sptech.school.backend.entity.Agendamento;

public class AgendamentoMapper {

    public static AgendamentoResponseDto toResponse(Agendamento agendamento) {

        AgendamentoResponseDto dto = new AgendamentoResponseDto();

        dto.setId(agendamento.getId());
        dto.setDataHora(agendamento.getDataHora());
        dto.setClienteNome(agendamento.getCliente().getNome());

        return dto;
    }
}