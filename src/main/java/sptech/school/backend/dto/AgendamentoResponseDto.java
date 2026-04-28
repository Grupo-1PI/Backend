package sptech.school.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "AgendamentoResponse", description = "Representação completa do agendamento após criado")
public class AgendamentoResponseDto {

    @Schema(description = "ID do agendamento registrado no banco", example = "1")
    private Long id;
    @Schema(description = "Data e hora confirmada", example = "2024-12-25T14:30:00")
    private LocalDateTime dataHora;
    @Schema(description = "Nome do cliente vinculado", example = "Gustavo Keniti")
    private String clienteNome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }
}