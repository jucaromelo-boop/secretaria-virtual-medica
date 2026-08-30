package com.consultorio.canalwhatsapp.config;

import com.twilio.security.RequestValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TwilioSignatureFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TwilioSignatureFilter.class);

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.webhook.public-url}")
    private String publicUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!request.getRequestURI().contains("/api/whatsapp/webhook")) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        String signature = request.getHeader("X-Twilio-Signature");
        if (signature == null) {
            log.warn("Peticion al webhook sin firma de Twilio, rechazada");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Firma de Twilio faltante");
            return;
        }

        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values.length > 0) {
                params.put(key, values[0]);
            }
        });

        RequestValidator validator = new RequestValidator(authToken);
        boolean esValida = validator.validate(publicUrl, params, signature);

        if (!esValida) {
            log.warn("Firma de Twilio invalida, peticion rechazada");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Firma de Twilio invalida");
            return;
        }

        filterChain.doFilter(wrappedRequest, response);
    }
}