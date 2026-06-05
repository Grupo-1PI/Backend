package sptech.school.backend.dto.EnderecoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Endereco", description = "Endereco completo enviado no cadastro de usuario")
public class EnderecoDto {

    @NotBlank
    @Schema(description = "CEP", example = "01310-100")
    private String cep;

    @NotBlank
    @Schema(description = "Logradouro", example = "Avenida Paulista")
    private String logradouro;

    @NotBlank
    @Schema(description = "Bairro", example = "Bela Vista")
    private String bairro;

    @NotBlank
    @Schema(description = "Cidade", example = "Sao Paulo")
    private String cidade;

    @NotBlank
    @Schema(description = "UF", example = "SP")
    private String uf;

    @NotBlank
    @Schema(description = "Numero", example = "1000")
    private String numero;

    @Schema(description = "Complemento", example = "Apto 42")
    private String complemento;

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
