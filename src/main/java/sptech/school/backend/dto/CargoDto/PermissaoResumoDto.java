package sptech.school.backend.dto.CargoDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO resumido de permissão")
public class PermissaoResumoDto {

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
