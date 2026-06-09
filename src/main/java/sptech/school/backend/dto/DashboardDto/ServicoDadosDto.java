package sptech.school.backend.dto.DashboardDto;

public class ServicoDadosDto {

    private String nome;
    private Long total;

    public ServicoDadosDto() {
    }

    public ServicoDadosDto(String nome, Long total) {
        this.nome = nome;
        this.total = total;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
