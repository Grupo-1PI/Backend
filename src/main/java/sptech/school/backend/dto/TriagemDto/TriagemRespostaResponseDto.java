package sptech.school.backend.dto.TriagemDto;

import io.swagger.v3.oas.annotations.media.Schema;
import sptech.school.backend.entity.RespostaEnum;

@Schema(description = "DTO de resposta da resposta da triagem")
public class TriagemRespostaResponseDto {

    private Long id;
    private RespostaEnum resposta;
    private TriagemPerguntaResponseDto proximaPergunta;
    private EspecialidadeTriagemResponseDto especialidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RespostaEnum getResposta() {
        return resposta;
    }

    public void setResposta(RespostaEnum resposta) {
        this.resposta = resposta;
    }

    public TriagemPerguntaResponseDto getProximaPergunta() {
        return proximaPergunta;
    }

    public void setProximaPergunta(TriagemPerguntaResponseDto proximaPergunta) {
        this.proximaPergunta = proximaPergunta;
    }

    public EspecialidadeTriagemResponseDto getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(EspecialidadeTriagemResponseDto especialidade) {
        this.especialidade = especialidade;
    }
}