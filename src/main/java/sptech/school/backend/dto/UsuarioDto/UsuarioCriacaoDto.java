package sptech.school.backend.dto.UsuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "DTO para criação de usuário")
public class UsuarioCriacaoDto {

    @Schema(example = "Felipe")
    private String nome;

    @Schema(example = "11999999999")
    private String telefone;

    @Schema(example = "felipe@email.com")
    private String email;

    @Schema(example = "123456")
    private String senha;

    @Schema(example = "2000-05-10")
    private LocalDate dataNascimento;

    @Schema(example = "1", description = "ID do endere\u00e7o (fkEndereco). Se omitido, usa o endere\u00e7o 1 do seed do banco.")
    private Long enderecoId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Long getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Long enderecoId) {
        this.enderecoId = enderecoId;
    }
}
