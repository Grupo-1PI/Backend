package sptech.school.backend.dto.EspecialidadeDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "DTO para edicao de especialidade")
public class EspecialidadeEdicaoDto {

    @NotBlank
    @Size(min = 3, max = 60)
    @Schema(description = "Nome da especialidade", example = "Massoterapia")
    private String nome;

    @Schema(description = "IDs dos servicos vinculados a especialidade", example = "[1, 2]")
    private List<Long> servicoIds;

    @Schema(description = "IDs dos funcionarios que possuem essa especialidade", example = "[1, 3]")
    private List<Long> funcionarioIds;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Long> getServicoIds() {
        return servicoIds;
    }

    public void setServicoIds(List<Long> servicoIds) {
        this.servicoIds = servicoIds;
    }

    public List<Long> getFuncionarioIds() {
        return funcionarioIds;
    }

    public void setFuncionarioIds(List<Long> funcionarioIds) {
        this.funcionarioIds = funcionarioIds;
    }
}
