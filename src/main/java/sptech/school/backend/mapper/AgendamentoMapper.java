package sptech.school.backend.mapper;

import sptech.school.backend.dto.AgendamentoDto.AgendamentoResponseDto;
import sptech.school.backend.entity.Agendamento;

public class AgendamentoMapper {

    public static AgendamentoResponseDto toResponse(Agendamento agendamento) {

        AgendamentoResponseDto dto = new AgendamentoResponseDto();

        dto.setId(agendamento.getId());
        dto.setDataHoraInicio(agendamento.getDataHoraInicio());
        dto.setDataHoraFim(agendamento.getDataHoraFim());

        dto.setClienteNome(agendamento.getCliente().getUsuario().getNome());
        dto.setFuncionarioNome(agendamento.getFuncionario().getUsuario().getNome());
        dto.setSalaDescricao(agendamento.getSala().getDescricao());
        dto.setServicoNome(agendamento.getServico().getNome());
        dto.setStatusNome(agendamento.getStatus().getNome());

        return dto;
    }
}