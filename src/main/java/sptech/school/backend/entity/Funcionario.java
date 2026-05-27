package sptech.school.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "funcionario")
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

    @ManyToMany
    @JoinTable(
            name = "funcionario_especialidade",
            joinColumns = @JoinColumn(name = "fkFuncionario"),
            inverseJoinColumns = @JoinColumn(name = "fkEspecialidade")
    )
    private List<Especialidade> especialidades = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Especialidade> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<Especialidade> especialidades) {
        this.especialidades = especialidades;
    }
}
