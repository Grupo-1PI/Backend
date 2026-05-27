package sptech.school.backend.dto.DisponibilidadeDto;

public class HorarioDisponivelDto {

    private String horario;
    private boolean disponivel;

    public HorarioDisponivelDto() {
    }

    public HorarioDisponivelDto(String horario, boolean disponivel) {
        this.horario = horario;
        this.disponivel = disponivel;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
