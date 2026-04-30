package sptech.school.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Schema(name = "Agendamento", description = "Representa um agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1")
    private Long id;

    @Column(name = "data_hora_inicio")
    @Schema(example = "2026-05-01T14:00:00")
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim")
    @Schema(example = "2026-05-01T15:00:00")
    private LocalDateTime dataHoraFim;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "fkCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "fkFuncionario")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "fkSala")
    private Sala sala;

    @ManyToOne
    @JoinColumn(name = "fkServico")
    private Servico servico;

    @ManyToOne
    @JoinColumn(name = "fkStatus")
    private Status status;

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}