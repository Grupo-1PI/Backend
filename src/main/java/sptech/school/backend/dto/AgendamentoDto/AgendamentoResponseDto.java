package sptech.school.backend.dto.AgendamentoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "DTO de resposta de agendamento")
public class AgendamentoResponseDto {

    private Long id;

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String observacao;

    private String clienteNome;
    private List<String> funcionarios = new ArrayList<>();
    private String salaDescricao;
    private List<String> servicos = new ArrayList<>();
    private String statusNome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public List<String> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<String> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public String getSalaDescricao() {
        return salaDescricao;
    }

    public void setSalaDescricao(String salaDescricao) {
        this.salaDescricao = salaDescricao;
    }

    public List<String> getServicos() {
        return servicos;
    }

    public void setServicos(List<String> servicos) {
        this.servicos = servicos;
    }

    public String getStatusNome() {
        return statusNome;
    }

    public void setStatusNome(String statusNome) {
        this.statusNome = statusNome;
    }
}
