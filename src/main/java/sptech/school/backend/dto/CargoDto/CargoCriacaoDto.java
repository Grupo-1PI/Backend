package sptech.school.backend.dto.CargoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "DTO para criação e atualização de cargo")
public class CargoCriacaoDto {

    @NotBlank
    @Size(max = 100)
    @Schema(example = "Administrador")
    private String nome;

    @Size(max = 255)
    @Schema(example = "Acesso total ao sistema")
    private String descricao;

    @Schema(example = "[1, 2, 3]")
    private List<Long> permissoesIds;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Long> getPermissoesIds() {
        return permissoesIds;
    }

    public void setPermissoesIds(List<Long> permissoesIds) {
        this.permissoesIds = permissoesIds;
    }
}
