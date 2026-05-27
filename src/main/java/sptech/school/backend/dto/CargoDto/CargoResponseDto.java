package sptech.school.backend.dto.CargoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO de resposta do cargo")
public class CargoResponseDto {

    private Long id;
    private String nome;
    private List<String> usuarios;
    private List<PermissaoResumoDto> permissoes;

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

    public List<String> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<String> usuarios) {
        this.usuarios = usuarios;
    }

    public List<PermissaoResumoDto> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<PermissaoResumoDto> permissoes) {
        this.permissoes = permissoes;
    }
}
