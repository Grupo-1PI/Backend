package sptech.school.backend.dto.TriagemDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import sptech.school.backend.entity.RespostaEnum;

@Schema(description = "Requisição para responder uma pergunta da triagem")
public class RespostaTriagemRequestDto {

    @NotNull
    @Schema(description = "ID da pergunta respondida", example = "1")
    private Long perguntaId;

    @NotNull
    @Schema(description = "Resposta do usuário", example = "SIM", allowableValues = {"SIM", "NAO"})
    private RespostaEnum resposta;

    public Long getPerguntaId() {
        return perguntaId;
    }

    public void setPerguntaId(Long perguntaId) {
        this.perguntaId = perguntaId;
    }

    public RespostaEnum getResposta() {
        return resposta;
    }

    public void setResposta(RespostaEnum resposta) {
        this.resposta = resposta;
    }
}