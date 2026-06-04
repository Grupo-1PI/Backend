package sptech.school.backend.dto.DisponibilidadeDto;

public class DiaDisponivelDto {

    private String data;
    private String status;

    public DiaDisponivelDto() {
    }

    public DiaDisponivelDto(String data, String status) {
        this.data = data;
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
