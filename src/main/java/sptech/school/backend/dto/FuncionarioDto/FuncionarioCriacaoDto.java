package sptech.school.backend.dto.FuncionarioDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para criação de funcionário")
public class FuncionarioCriacaoDto {

    @Schema(example = "1")
    private Long usuarioId;

    @Schema(example = "1")
    private Long cargoId;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }
}