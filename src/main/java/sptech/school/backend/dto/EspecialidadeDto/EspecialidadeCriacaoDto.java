package sptech.school.backend.dto.EspecialidadeDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "DTO para criacao e atualizacao de especialidade")
public class EspecialidadeCriacaoDto {

    @NotBlank
    @Size(max = 60)
    @Schema(example = "Acupuntura")
    private String nome;

    @Schema(example = "[1, 2]")
    private List<Long> servicosIds;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Long> getServicosIds() {
        return servicosIds;
    }

    public void setServicosIds(List<Long> servicosIds) {
        this.servicosIds = servicosIds;
    }
}
