package sptech.school.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class LimiteTentativasLoginTest {

    @Test
    void deveBloquearAposCincoTentativasInvalidas() {
        LimiteTentativasLogin limite = new LimiteTentativasLogin(5, 15);

        for (int i = 0; i < 5; i++) {
            limite.registrarFalha("usuario@email.com");
        }

        assertThrows(
                ResponseStatusException.class,
                () -> limite.verificar("usuario@email.com")
        );
    }
}
