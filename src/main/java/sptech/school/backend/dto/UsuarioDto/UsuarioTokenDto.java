package sptech.school.backend.dto.UsuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Usuário - Token", description = "Objeto de retorno contendo os dados do usuário e o token JWT gerado")
public class UsuarioTokenDto {

    @Schema(description = "Identificador único do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long usuarioId;

    @Schema(description = "Nome do usuário autenticado", example = "Fernanda Henckel")
    private String nome;

    @Schema(description = "E-mail do usuário autenticado", example = "fernanda.henckel@gmail.com")
    private String email;

    @Schema(description = "Token JWT de autenticação (Bearer)", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmZWxpcGVAZW1haWwuY29tIiwiaWF0Ijo...")
    private String token;
    private Long clienteId;
    private Long funcionarioId;
    private String tipo;
    private CargoResumoDto cargo;
    private List<PermissaoResumoDto> permissoes = new ArrayList<>();

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public CargoResumoDto getCargo() {
        return cargo;
    }

    public void setCargo(CargoResumoDto cargo) {
        this.cargo = cargo;
    }

    public List<PermissaoResumoDto> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<PermissaoResumoDto> permissoes) {
        this.permissoes = permissoes;
    }

    public static class CargoResumoDto {

        private Long id;
        private String nome;

        public CargoResumoDto() {
        }

        public CargoResumoDto(Long id, String nome) {
            this.id = id;
            this.nome = nome;
        }

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

    public static class PermissaoResumoDto {

        private Long id;
        private String nome;

        public PermissaoResumoDto() {
        }

        public PermissaoResumoDto(Long id, String nome) {
            this.id = id;
            this.nome = nome;
        }

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
}
