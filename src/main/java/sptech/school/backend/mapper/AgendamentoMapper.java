package sptech.school.backend.mapper;

import sptech.school.backend.dto.AgendamentoDto.AgendamentoResponseDto;
import sptech.school.backend.entity.Agendamento;
import sptech.school.backend.entity.AtendimentoServico;
import sptech.school.backend.entity.FuncionarioAgendamento;

public class AgendamentoMapper {

    public static AgendamentoResponseDto toResponse(Agendamento agendamento) {

        AgendamentoResponseDto dto = new AgendamentoResponseDto();

        dto.setId(agendamento.getId());
        dto.setDataHoraInicio(agendamento.getDataHoraInicio());
        dto.setDataHoraFim(agendamento.getDataHoraFim());
        dto.setObservacao(agendamento.getObservacao());

        dto.setClienteNome(agendamento.getCliente().getUsuario().getNome());
        dto.setFuncionarios(
                agendamento.getFuncionarioAgendamentos()
                        .stream()
                        .map(FuncionarioAgendamento::getFuncionario)
                        .map(funcionario -> funcionario.getUsuario().getNome())
                        .toList()
        );
        dto.setSalaDescricao(agendamento.getSala().getDescricao());
        dto.setServicos(
                agendamento.getAtendimentoServicos()
                        .stream()
                        .map(AtendimentoServico::getServico)
                        .map(servico -> servico.getNome())
                        .toList()
        );
        dto.setStatusNome(agendamento.getStatus().getNome());

        return dto;
    }
}
