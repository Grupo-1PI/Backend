package sptech.school.backend.dto.CargoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO para criação e atualização de cargo")
public class CargoCriacaoDto {

    @Schema(example = "Administrador")
    private String nome;

    @Schema(example = "Acesso total ao sistema")
    private String descricao;

    @Schema(example = "[1, 2, 3]")
    private List<Long> permissaoIds;

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

    public List<Long> getPermissaoIds() {
        return permissaoIds;
    }

    public void setPermissaoIds(List<Long> permissaoIds) {
        this.permissaoIds = permissaoIds;
    }
}
