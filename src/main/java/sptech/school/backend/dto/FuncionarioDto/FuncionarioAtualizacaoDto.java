package sptech.school.backend.dto.FuncionarioDto;

import java.util.ArrayList;
import java.util.List;

public class FuncionarioAtualizacaoDto {

    private Long cargoId;
    private List<Long> especialidadesIds = new ArrayList<>();

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public List<Long> getEspecialidadesIds() {
        return especialidadesIds;
    }

    public void setEspecialidadesIds(List<Long> especialidadesIds) {
        this.especialidadesIds = especialidadesIds;
    }
}
