package sptech.school.backend.dto.AgendaDto;

import java.util.List;

public class AgendaFuncionarioListagemDto {

    private Long funcionarioId;
    private String funcionarioNome;
    private String especialidadePrincipal;
    private List<AgendaItemDto> agendas;

    public AgendaFuncionarioListagemDto() {
    }

    public AgendaFuncionarioListagemDto(
            Long funcionarioId,
            String funcionarioNome,
            String especialidadePrincipal,
            List<AgendaItemDto> agendas
    ) {
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
        this.especialidadePrincipal = especialidadePrincipal;
        this.agendas = agendas;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getFuncionarioNome() {
        return funcionarioNome;
    }

    public void setFuncionarioNome(String funcionarioNome) {
        this.funcionarioNome = funcionarioNome;
    }

    public String getEspecialidadePrincipal() {
        return especialidadePrincipal;
    }

    public void setEspecialidadePrincipal(String especialidadePrincipal) {
        this.especialidadePrincipal = especialidadePrincipal;
    }

    public List<AgendaItemDto> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<AgendaItemDto> agendas) {
        this.agendas = agendas;
    }

    public static class AgendaItemDto {

        private Long id;
        private Integer diaSemana;
        private String horaInicio;
        private String horaFim;

        public AgendaItemDto() {
        }

        public AgendaItemDto(Long id, Integer diaSemana, String horaInicio, String horaFim) {
            this.id = id;
            this.diaSemana = diaSemana;
            this.horaInicio = horaInicio;
            this.horaFim = horaFim;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getDiaSemana() {
            return diaSemana;
        }

        public void setDiaSemana(Integer diaSemana) {
            this.diaSemana = diaSemana;
        }

        public String getHoraInicio() {
            return horaInicio;
        }

        public void setHoraInicio(String horaInicio) {
            this.horaInicio = horaInicio;
        }

        public String getHoraFim() {
            return horaFim;
        }

        public void setHoraFim(String horaFim) {
            this.horaFim = horaFim;
        }
    }
}
