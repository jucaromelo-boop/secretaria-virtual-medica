package com.consultorio.orquestadoria.client;

import com.consultorio.orquestadoria.client.dto.ClaudeMessage;
import com.consultorio.orquestadoria.client.dto.ClaudeRequest;
import com.consultorio.orquestadoria.client.dto.ClaudeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ClaudeClient {

    private final RestTemplate restTemplate;

    @Value("${anthropic.api-key}")
    private String apiKey;

    @Value("${anthropic.model}")
    private String model;

    @Value("${anthropic.api-url}")
    private String apiUrl;

    public ClaudeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String enviarMensaje(String systemPrompt, List<ClaudeMessage> historial) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");
        headers.setContentType(MediaType.APPLICATION_JSON);

        ClaudeRequest request = new ClaudeRequest(model, 1024, systemPrompt, historial);

        HttpEntity<ClaudeRequest> entity = new HttpEntity<>(request, headers);

        ClaudeResponse response = restTemplate.postForObject(apiUrl, entity, ClaudeResponse.class);

        if (response == null || response.getContent() == null || response.getContent().isEmpty()) {
            return "Disculpa, tuve un problema para procesar tu mensaje. ¿Puedes intentar de nuevo?";
        }

        return response.getContent().get(0).getText();
    }
}