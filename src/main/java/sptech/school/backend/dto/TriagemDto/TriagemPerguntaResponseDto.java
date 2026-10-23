package sptech.school.backend.dto.TriagemDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO de resposta da pergunta da triagem")
public class TriagemPerguntaResponseDto {

    private Long id;
    private String texto;
    private Integer ordem;
    private Boolean raiz;
    private EspecialidadeTriagemResponseDto especialidade;
    private List<TriagemRespostaResponseDto> respostas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Boolean getRaiz() {
        return raiz;
    }

    public void setRaiz(Boolean raiz) {
        this.raiz = raiz;
    }

    public EspecialidadeTriagemResponseDto getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(EspecialidadeTriagemResponseDto especialidade) {
        this.especialidade = especialidade;
    }

    public List<TriagemRespostaResponseDto> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<TriagemRespostaResponseDto> respostas) {
        this.respostas = respostas;
    }
}