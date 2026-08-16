package com.consultorio.notificaciones.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CanalWhatsappClient {

    private static final Logger log = LoggerFactory.getLogger(CanalWhatsappClient.class);

    private final RestTemplate restTemplate;

    public CanalWhatsappClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void enviarMensaje(String numeroDestino, String mensaje) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Map.of("numeroDestino", numeroDestino, "mensaje", mensaje);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            restTemplate.postForObject("http://ms-canal-whatsapp/api/whatsapp/enviar", entity, Void.class);
            log.info("Notificacion WhatsApp enviada a {}", numeroDestino);
        } catch (Exception ex) {
            log.error("Error enviando notificacion WhatsApp a {}: {}", numeroDestino, ex.getMessage());
        }
    }
}