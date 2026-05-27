package sptech.school.backend.dto.FuncionarioDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "DTO de resposta do funcionário")
public class FuncionarioResponseDto {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cargo;
    private List<String> especialidades = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCargo() {
        return cargo;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }
}
