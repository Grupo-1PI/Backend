package sptech.school.backend.exception;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class IntegridadeRequestFilter extends OncePerRequestFilter {

    private static final String MENSAGEM_BODY_VAZIO = "{\"mensagem\": \"Request body não pode ser vazio\"}";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!request.getMethod().equalsIgnoreCase("POST") && !request.getMethod().equalsIgnoreCase("PUT")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!isJson(request.getContentType())) {
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        if (new String(body, StandardCharsets.UTF_8).isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(MENSAGEM_BODY_VAZIO);
            return;
        }

        HttpServletRequest replayableRequest = new ReplayableRequestWrapper(request, body);
        ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(replayableRequest);
        filterChain.doFilter(cachingRequest, response);
    }

    private boolean isJson(String contentType) {
        if (contentType == null) {
            return false;
        }

        try {
            return MediaType.APPLICATION_JSON.isCompatibleWith(MediaType.parseMediaType(contentType));
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private static class ReplayableRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] body;

        private ReplayableRequestWrapper(HttpServletRequest request, byte[] body) {
            super(request);
            this.body = body;
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override
                public int read() {
                    return inputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return inputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    // Leitura síncrona usada pela API REST.
                }
            };
        }
    }
}