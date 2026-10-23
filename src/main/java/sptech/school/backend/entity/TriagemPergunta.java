package sptech.school.backend.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "triagem_pergunta")
public class TriagemPergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String texto;

    private Integer ordem = 0;

    private Boolean raiz = false;

    @ManyToOne
    @JoinColumn(name = "fk_especialidade")
    private Especialidade especialidade;

    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TriagemResposta> respostas = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Boolean getRaiz() {
        return raiz;
    }

    public void setRaiz(Boolean raiz) {
        this.raiz = raiz;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public List<TriagemResposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<TriagemResposta> respostas) {
        this.respostas = respostas;
    }
}