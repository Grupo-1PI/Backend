package sptech.school.backend.dto.ServicoDto;

import sptech.school.backend.entity.Sala;

import java.math.BigDecimal;
import java.util.List;

public class ServicoResponseDto {
    private Long id;
    private String nome;
    private BigDecimal valor;
    private String descricao;
    private Integer tempoMedio;
    private List<Sala> salas;

    public ServicoResponseDto(Long id, String nome, BigDecimal valor, String descricao, Integer tempoMedio, List<Sala> salas) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.descricao = descricao;
        this.tempoMedio = tempoMedio;
        this.salas = salas;
    }

    public ServicoResponseDto() {
    }

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

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }
}
