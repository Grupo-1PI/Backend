package sptech.school.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(name = "Funcionario", description = "Representa um funcionário da clínica")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fkUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "fkCargo", nullable = false)
    private Cargo cargo;

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}