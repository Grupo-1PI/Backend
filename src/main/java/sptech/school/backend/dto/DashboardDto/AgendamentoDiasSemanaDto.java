package sptech.school.backend.dto.DashboardDto;

public class AgendamentoDiasSemanaDto {

    private Integer diaSemana;
    private Long total;

    public AgendamentoDiasSemanaDto(Integer diaSemana, Long total) {
        this.diaSemana = diaSemana;
        this.total = total;
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
