package sptech.school.backend.dto.DashboardDto;

import java.time.LocalDate;

public class RetornoPacientesDto {

    private LocalDate inicio;
    private LocalDate fim;
    private Long pacientes;
    private Long retornantes;
    private Double taxa;

    public RetornoPacientesDto() {
    }

    public RetornoPacientesDto(LocalDate inicio, LocalDate fim, Long pacientes, Long retornantes, Double taxa) {
        this.inicio = inicio;
        this.fim = fim;
        this.pacientes = pacientes;
        this.retornantes = retornantes;
        this.taxa = taxa;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFim() {
        return fim;
    }

    public void setFim(LocalDate fim) {
        this.fim = fim;
    }

    public Long getPacientes() {
        return pacientes;
    }

    public void setPacientes(Long pacientes) {
        this.pacientes = pacientes;
    }

    public Long getRetornantes() {
        return retornantes;
    }

    public void setRetornantes(Long retornantes) {
        this.retornantes = retornantes;
    }

    public Double getTaxa() {
        return taxa;
    }

    public void setTaxa(Double taxa) {
        this.taxa = taxa;
    }
}
