package sptech.school.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "AgendamentoRequest", description = "Dados para criação de um novo agendamento")
public class AgendamentoRequestDto {

    @Schema(description = "Data e hora do agendamento", example = "2024-12-25T14:30:00")
    private LocalDateTime dataHora;

    @Schema(description = "ID do cliente que será vinculado", example = "1")
    private Long clienteId;

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}