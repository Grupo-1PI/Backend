package sptech.school.backend.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionario_agendamento")
public class FuncionarioAgendamento {

    @EmbeddedId
    private FuncionarioAgendamentoId id;

    @ManyToOne
    @MapsId("funcionarioId")
    @JoinColumn(name = "fkFuncionario")
    private Funcionario funcionario;

    @ManyToOne
    @MapsId("agendamentoId")
    @JoinColumn(name = "fkAgendamento")
    private Agendamento agendamento;

    public FuncionarioAgendamentoId getId() {
        return id;
    }

    public void setId(FuncionarioAgendamentoId id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }
}
