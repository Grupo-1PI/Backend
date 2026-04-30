package sptech.school.backend.dto.AgendamentoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO para criação de agendamento")
public class AgendamentoRequestDto {

    @Schema(example = "2026-05-01T14:00:00")
    private LocalDateTime dataHoraInicio;

    @Schema(example = "2026-05-01T15:00:00")
    private LocalDateTime dataHoraFim;

    @Schema(example = "Sessão inicial")
    private String observacao;

    @Schema(example = "1")
    private Long clienteId;

    @Schema(example = "1")
    private Long funcionarioId;

    @Schema(example = "1")
    private Long salaId;

    @Schema(example = "1")
    private Long servicoId;

    @Schema(example = "1")
    private Long statusId;

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Long getSalaId() {
        return salaId;
    }

    public void setSalaId(Long salaId) {
        this.salaId = salaId;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
}