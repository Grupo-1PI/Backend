package sptech.school.backend.dto.ServicoDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public class ServicoCriacaoDto {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal valor;

    @NotBlank
    @Size(max = 255)
    private String descricao;

    private Integer tempoMedio;
    private List<Long> salasIds;

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
