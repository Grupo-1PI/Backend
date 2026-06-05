package sptech.school.backend.dto.EspecialidadeDto;

import io.swagger.v3.oas.annotations.media.Schema;
import sptech.school.backend.dto.ServicoDto.ServicoResumoDto;
import java.util.List;

@Schema(description = "DTO de resposta da especialidade")
public class EspecialidadeResponseDto {

    private Long id;
    private String nome;
    private List<ServicoResumoDto> servicos;

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

    public List<ServicoResumoDto> getServicos() {
        return servicos;
    }

    public void setServicos(List<ServicoResumoDto> servicos) {
        this.servicos = servicos;
    }
}
