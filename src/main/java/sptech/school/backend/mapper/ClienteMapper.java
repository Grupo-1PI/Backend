package sptech.school.backend.mapper;

import sptech.school.backend.dto.ClienteDto.ClienteResponseDto;
import sptech.school.backend.entity.Cliente;

public class ClienteMapper {

    public static ClienteResponseDto toResponse(Cliente cliente) {
        ClienteResponseDto dto = new ClienteResponseDto();

        dto.setId(cliente.getId());
        dto.setNome(cliente.getUsuario().getNome());
        dto.setEmail(cliente.getUsuario().getEmail());
        dto.setTelefone(cliente.getUsuario().getTelefone());
        dto.setObservacao(cliente.getObservacao());

        return dto;
    }
}
