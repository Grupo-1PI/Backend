package sptech.school.backend.dto.NotificacaoDto;

import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificacaoSaasRequest(
        @NotBlank @JsonProperty("telefone") String telefone
) { }