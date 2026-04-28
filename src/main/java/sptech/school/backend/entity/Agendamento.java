package sptech.school.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.ScriptAssert;

import java.time.LocalDateTime;

@Entity
@Schema(name = "Agendamento", description = "Dados do agendamento com cliente")
public class Agendamento {

    @Schema(description = "ID do agendamento", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Data e hora do agendamento", example = "2023-12-25T14:30:00")
    private LocalDateTime dataHora;

    @Schema(description = "Dados do cliente vinculado ao agendamento")
    @ManyToOne
    @JoinColumn(name = "fk_cliente")
    private Cliente cliente;

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}

