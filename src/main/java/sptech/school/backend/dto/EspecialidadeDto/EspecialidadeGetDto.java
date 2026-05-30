package sptech.school.backend.dto.EspecialidadeDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de resposta de especialidade")
public class EspecialidadeGetDto {

    @Schema(description = "ID da especialidade", example = "1")
    private Long id;

    @Schema(description = "Nome da especialidade", example = "Acupuntura")
    private String nome;

    @Schema(description = "Servicos vinculados a especialidade")
    private List<ServicoResumoDto> servicos;

    @Schema(description = "Funcionarios vinculados a especialidade")
    private List<FuncionarioResumoDto> funcionarios;

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

    public List<FuncionarioResumoDto> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<FuncionarioResumoDto> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public static class ServicoResumoDto {

        @Schema(description = "ID do servico", example = "1")
        private Long id;

        @Schema(description = "Nome do servico", example = "Sessao de Acupuntura")
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

    public static class FuncionarioResumoDto {

        @Schema(description = "ID do funcionario", example = "1")
        private Long id;

        @Schema(description = "Nome do usuario vinculado ao funcionario", example = "Felipe Silva")
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
}
