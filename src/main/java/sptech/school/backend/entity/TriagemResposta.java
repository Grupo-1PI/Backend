package sptech.school.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "triagem_resposta",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_pergunta", "resposta"}))
public class TriagemResposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_pergunta", nullable = false)
    private TriagemPergunta pergunta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private RespostaEnum resposta;

    @ManyToOne
    @JoinColumn(name = "fk_proxima_pergunta")
    private TriagemPergunta proximaPergunta;

    @ManyToOne
    @JoinColumn(name = "fk_especialidade")
    private Especialidade especialidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TriagemPergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(TriagemPergunta pergunta) {
        this.pergunta = pergunta;
    }

    public RespostaEnum getResposta() {
        return resposta;
    }

    public void setResposta(RespostaEnum resposta) {
        this.resposta = resposta;
    }

    public TriagemPergunta getProximaPergunta() {
        return proximaPergunta;
    }

    public void setProximaPergunta(TriagemPergunta proximaPergunta) {
        this.proximaPergunta = proximaPergunta;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }
}