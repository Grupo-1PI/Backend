package sptech.school.backend.dto.ServicoDto;

import java.math.BigDecimal;
import java.util.List;

public class ServicoRequestDto {
    private String nome;
    private BigDecimal valor;
    private String descricao;
    private Integer tempoMedio;
    private List<Long> salasIds;

    public ServicoRequestDto(String nome, BigDecimal valor, String descricao, Integer tempoMedio, List<Long> salasIds) {
        this.nome = nome;
        this.valor = valor;
        this.descricao = descricao;
        this.tempoMedio = tempoMedio;
        this.salasIds = salasIds;
    }

    public ServicoRequestDto() {
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

    public List<Long> getSalasIds() {
        return salasIds;
    }

    public void setSalasIds(List<Long> salasIds) {
        this.salasIds = salasIds;
    }
}
