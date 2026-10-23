package sptech.school.backend.dto.TriagemDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import sptech.school.backend.entity.RespostaEnum;

@Schema(description = "DTO para criação de resposta da triagem")
public class TriagemRespostaCriacaoDto {

    @NotNull
    @Schema(description = "Resposta do usuário", example = "SIM", allowableValues = {"SIM", "NAO"})
    private RespostaEnum resposta;

    @Schema(description = "ID da próxima pergunta (se não for fim)", example = "2")
    private Long proximaPerguntaId;

    @Schema(description = "ID da especialidade final (se for fim)", example = "1")
    private Long especialidadeId;

    public RespostaEnum getResposta() { return resposta; }
    public void setResposta(RespostaEnum resposta) { this.resposta = resposta; }

    public Long getProximaPerguntaId() { return proximaPerguntaId; }
    public void setProximaPerguntaId(Long proximaPerguntaId) { this.proximaPerguntaId = proximaPerguntaId; }

    public Long getEspecialidadeId() { return especialidadeId; }
    public void setEspecialidadeId(Long especialidadeId) { this.especialidadeId = especialidadeId; }
}