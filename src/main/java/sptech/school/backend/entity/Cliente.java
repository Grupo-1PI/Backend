package sptech.school.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
@Schema(name = "cliente", description = "Representa um cliente do sistema")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do cliente", example = "1")
    private Long id;

    @OneToOne
    @JoinColumn(name = "fkUsuario")
    @Schema(description = "Usuário vinculado ao cliente")
    private Usuario usuario;

    @Schema(description = "Observação do cliente", example = "Cliente VIP")
    private String observacao;

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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
