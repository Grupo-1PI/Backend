package sptech.school.backend.dto.SalaDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SalaCriacaoDto {

    @NotBlank
    @Size(max = 45)
    private String descricao;

    public SalaCriacaoDto(){}

    public SalaCriacaoDto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
