package sptech.school.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "servico")
@Schema(name = "Servico", description = "Representa um serviço oferecido")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1")
    private Long id;

    @Schema(example = "Sessão de Acupuntura")
    private String nome;

    @Schema(example = "120.00")
    private BigDecimal valor;

    @Schema(example = "Sessão padrão")
    private String descricao;

    @Schema(example = "60")
    private Integer tempoMedio;

    @ManyToMany
    @JoinTable(
            name = "sala_servico",
            joinColumns = @JoinColumn(name = "fkServico"),
            inverseJoinColumns = @JoinColumn(name = "fkSala")
    )
    private List<Sala> salas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getTempoMedio() {
        return tempoMedio;
    }

    public void setTempoMedio(Integer tempoMedio) {
        this.tempoMedio = tempoMedio;
    }

    public List<Sala> getSalas() { return salas; }

}
