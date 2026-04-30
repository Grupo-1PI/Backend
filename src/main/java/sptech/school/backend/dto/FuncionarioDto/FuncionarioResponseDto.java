package sptech.school.backend.dto.FuncionarioDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta do funcionário")
public class FuncionarioResponseDto {

    private Long id;
    private String nomeUsuario;
    private String cargo;

    public Long getId() {
        return id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}