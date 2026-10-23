package sptech.school.backend.mapper;

import sptech.school.backend.dto.TriagemDto.EspecialidadeTriagemResponseDto;
import sptech.school.backend.dto.TriagemDto.TriagemPerguntaResponseDto;
import sptech.school.backend.dto.TriagemDto.TriagemRespostaResponseDto;
import sptech.school.backend.entity.Especialidade;
import sptech.school.backend.entity.TriagemPergunta;
import sptech.school.backend.entity.TriagemResposta;
import java.util.stream.Collectors;

public class TriagemMapper {

    public static TriagemPerguntaResponseDto toPerguntaResponse(TriagemPergunta pergunta) {
        TriagemPerguntaResponseDto dto = new TriagemPerguntaResponseDto();
        dto.setId(pergunta.getId());
        dto.setTexto(pergunta.getTexto());
        dto.setOrdem(pergunta.getOrdem());
        dto.setRaiz(pergunta.getRaiz());
        if (pergunta.getEspecialidade() != null) {
            dto.setEspecialidade(toEspecialidadeResponse(pergunta.getEspecialidade()));
        }
        if (pergunta.getRespostas() != null) {
            dto.setRespostas(pergunta.getRespostas().stream()
                    .map(TriagemMapper::toRespostaResponse)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static TriagemRespostaResponseDto toRespostaResponse(TriagemResposta resposta) {
        TriagemRespostaResponseDto dto = new TriagemRespostaResponseDto();
        dto.setId(resposta.getId());
        dto.setResposta(resposta.getResposta());
        if (resposta.getProximaPergunta() != null) {
            dto.setProximaPergunta(toPerguntaResponse(resposta.getProximaPergunta()));
        }
        if (resposta.getEspecialidade() != null) {
            dto.setEspecialidade(toEspecialidadeResponse(resposta.getEspecialidade()));
        }
        return dto;
    }

    public static EspecialidadeTriagemResponseDto toEspecialidadeResponse(Especialidade especialidade) {
        EspecialidadeTriagemResponseDto dto = new EspecialidadeTriagemResponseDto();
        dto.setId(especialidade.getId());
        dto.setNome(especialidade.getNome());
        return dto;
    }
}