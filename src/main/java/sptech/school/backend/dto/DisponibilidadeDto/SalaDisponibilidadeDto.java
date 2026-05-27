package sptech.school.backend.dto.DisponibilidadeDto;

public class SalaDisponibilidadeDto {

    private Long id;
    private String descricao;
    private boolean ocupada;

    public SalaDisponibilidadeDto() {
    }

    public SalaDisponibilidadeDto(Long id, String descricao, boolean ocupada) {
        this.id = id;
        this.descricao = descricao;
        this.ocupada = ocupada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }
}
