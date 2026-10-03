package sptech.school.backend.dto.AgendaDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Resposta enxuta de uma excecao de agenda (folga, bloqueio ou liberacao).
 *
 * Existe para NAO devolver a entidade AgendaExcecao: a entidade carrega
 * Funcionario -> Cargo -> funcionarios -> Funcionario..., o que faz o JSON
 * crescer recursivamente e voltar truncado (e o front-end quebra ao fazer
 * .map na resposta).
 */
@Schema(description = "DTO de resposta de excecao de agenda")
public class AgendaExcecaoListagemDto {

    private Long id;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Boolean disponivel;

    public AgendaExcecaoListagemDto() {
    }

    public AgendaExcecaoListagemDto(
            Long id,
            LocalDate data,
            LocalTime horaInicio,
            LocalTime horaFim,
            Boolean disponivel
    ) {
        this.id = id;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.disponivel = disponivel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}