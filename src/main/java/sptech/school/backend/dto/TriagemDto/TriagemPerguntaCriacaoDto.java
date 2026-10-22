package sptech.school.backend.dto.TriagemDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "DTO para criação/atualização de pergunta da triagem")
public class TriagemPerguntaCriacaoDto {

    @NotBlank
    @Schema(description = "Texto da pergunta", example = "O paciente sente dor muscular?")
    private String texto;

    @Schema(description = "Ordem de exibição", example = "1")
    private Integer ordem = 0;

    @Schema(description = "Se é a pergunta raiz", example = "false")
    private Boolean raiz = false;

    @Schema(description = "ID da especialidade se for pergunta final (folha)", example = "1")
    private Long especialidadeId;

    @Schema(description = "Respostas possíveis (sim/não) com próxima pergunta ou especialidade")
    private List<TriagemRespostaCriacaoDto> respostas;

    // Getters e Setters
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public Boolean getRaiz() { return raiz; }
    public void setRaiz(Boolean raiz) { this.raiz = raiz; }

    public Long getEspecialidadeId() { return especialidadeId; }
    public void setEspecialidadeId(Long especialidadeId) { this.especialidadeId = especialidadeId; }

    public List<TriagemRespostaCriacaoDto> getRespostas() { return respostas; }
    public void setRespostas(List<TriagemRespostaCriacaoDto> respostas) { this.respostas = respostas; }
}