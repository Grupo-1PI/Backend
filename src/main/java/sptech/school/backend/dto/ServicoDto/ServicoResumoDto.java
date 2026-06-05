package sptech.school.backend.dto.ServicoDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO resumido de servico")
public class ServicoResumoDto {

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
