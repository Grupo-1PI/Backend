package sptech.school.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class HorarioIndisponivelException extends RuntimeException {

    public HorarioIndisponivelException() {
        super("Horário já está ocupado");
    }
}
