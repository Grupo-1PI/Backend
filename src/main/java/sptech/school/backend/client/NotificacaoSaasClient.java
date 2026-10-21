package sptech.school.backend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import sptech.school.backend.dto.NotificacaoDto.NotificacaoSaasRequest;

@Component
public class NotificacaoSaasClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificacaoSaasClient.class);

    private final WebClient webClient;

    public NotificacaoSaasClient(@Value("${notificacao.saas.url}/notificacoes") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public void enviar(String telefone) {
        NotificacaoSaasRequest request = new NotificacaoSaasRequest(telefone);

        try {
            webClient.post()
                    .bodyValue(request)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            LOGGER.info("[SAAS] Notificacao enviada para: {}", telefone);
        } catch (WebClientResponseException e) {
            LOGGER.error("[SAAS] Erro HTTP {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Falha ao enviar notificacao via SaaS", e);
        } catch (Exception e) {
            LOGGER.error("[SAAS] Erro inesperado: {}", e.getMessage());
            throw new RuntimeException("Falha ao enviar notificacao via SaaS", e);
        }
    }
}