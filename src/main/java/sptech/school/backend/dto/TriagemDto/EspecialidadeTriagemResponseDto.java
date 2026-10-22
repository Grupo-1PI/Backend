package sptech.school.backend.dto.TriagemDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta da especialidade da triagem")
public class EspecialidadeTriagemResponseDto {

    private Long id;
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}