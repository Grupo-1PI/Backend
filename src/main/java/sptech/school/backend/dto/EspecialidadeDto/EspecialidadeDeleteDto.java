package sptech.school.backend.dto.EspecialidadeDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de retorno apos exclusao de especialidade")
public class EspecialidadeDeleteDto {

    @Schema(description = "ID da especialidade excluida", example = "1")
    private Long id;

    @Schema(description = "Nome da especialidade excluida", example = "Acupuntura")
    private String nome;

    @Schema(description = "Mensagem de confirmacao da exclusao", example = "Especialidade excluida com sucesso")
    private String mensagem;

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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
