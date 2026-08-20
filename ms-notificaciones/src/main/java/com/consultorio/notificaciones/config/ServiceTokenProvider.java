package com.consultorio.notificaciones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Component
public class ServiceTokenProvider {

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.service-client-id}")
    private String clientId;

    @Value("${keycloak.service-client-secret}")
    private String clientSecret;

    private final RestTemplate plainRestTemplate = new RestTemplate();

    private String cachedToken;
    private Instant expiracion = Instant.EPOCH;

    public synchronized String obtenerToken() {
        if (cachedToken != null && Instant.now().isBefore(expiracion)) {
            return cachedToken;
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", "client_credentials");

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        org.springframework.http.HttpEntity<MultiValueMap<String, String>> request =
                new org.springframework.http.HttpEntity<>(body, headers);

        TokenResponse response = plainRestTemplate.postForObject(tokenUri, request, TokenResponse.class);

        if (response != null) {
            cachedToken = response.access_token;
            // Renovamos 10 segundos antes de que expire realmente, por margen de seguridad
            expiracion = Instant.now().plusSeconds(Math.max(response.expires_in - 10, 5));
        }

        return cachedToken;
    }

    private static class TokenResponse {
        public String access_token;
        public long expires_in;
    }
}