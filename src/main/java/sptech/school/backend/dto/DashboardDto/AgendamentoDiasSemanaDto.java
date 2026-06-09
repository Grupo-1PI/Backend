package sptech.school.backend.dto.DashboardDto;

public class AgendamentoDiasSemanaDto {

    private String diaSemana;
    private Long total;

    public AgendamentoDiasSemanaDto(String diaSemana, Long total) {
        this.diaSemana = diaSemana;
        this.total = total;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
