package sptech.school.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class LimiteTentativasLogin {

    private final int maxTentativas;
    private final Duration janela;
    private final Map<String, Tentativas> tentativas = new HashMap<>();

    public LimiteTentativasLogin(
            @Value("${security.login.max-attempts:5}") int maxTentativas,
            @Value("${security.login.window-minutes:15}") long minutosJanela
    ) {
        this.maxTentativas = maxTentativas;
        this.janela = Duration.ofMinutes(minutosJanela);
    }

    public synchronized void verificar(String email) {
        Tentativas registro = tentativas.get(normalizar(email));
        if (registro != null
                && registro.quantidade() >= maxTentativas
                && registro.inicio().plus(janela).isAfter(Instant.now())) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Muitas tentativas de login. Tente novamente mais tarde."
            );
        }
    }

    public synchronized void registrarFalha(String email) {
        String chave = normalizar(email);
        Instant agora = Instant.now();
        Tentativas registro = tentativas.get(chave);

        if (registro == null || registro.inicio().plus(janela).isBefore(agora)) {
            registro = new Tentativas(agora, 0);
        }

        registro = new Tentativas(registro.inicio(), registro.quantidade() + 1);
        tentativas.put(chave, registro);
    }

    public synchronized void limpar(String email) {
        tentativas.remove(normalizar(email));
    }

    private String normalizar(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private record Tentativas(Instant inicio, int quantidade) {}
}
