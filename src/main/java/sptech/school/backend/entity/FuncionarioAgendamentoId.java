package sptech.school.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FuncionarioAgendamentoId implements Serializable {

    @Column(name = "fkFuncionario")
    private Long funcionarioId;

    @Column(name = "fkAgendamento")
    private Long agendamentoId;

    public FuncionarioAgendamentoId() {
    }

    public FuncionarioAgendamentoId(Long funcionarioId, Long agendamentoId) {
        this.funcionarioId = funcionarioId;
        this.agendamentoId = agendamentoId;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(Long agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FuncionarioAgendamentoId that)) {
            return false;
        }
        return Objects.equals(funcionarioId, that.funcionarioId)
                && Objects.equals(agendamentoId, that.agendamentoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(funcionarioId, agendamentoId);
    }
}
