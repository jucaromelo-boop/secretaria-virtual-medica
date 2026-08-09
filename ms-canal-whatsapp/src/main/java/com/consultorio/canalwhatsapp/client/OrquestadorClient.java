package com.consultorio.canalwhatsapp.client;

import com.consultorio.canalwhatsapp.client.dto.OrquestadorRequest;
import com.consultorio.canalwhatsapp.client.dto.OrquestadorResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrquestadorClient {

    private static final String BASE_URL = "http://ms-orquestador-ia/api/orquestador/mensaje";

    private final RestTemplate restTemplate;

    public OrquestadorClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String obtenerRespuesta(String texto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        OrquestadorRequest request = new OrquestadorRequest(texto);
        HttpEntity<OrquestadorRequest> entity = new HttpEntity<>(request, headers);

        OrquestadorResponse response = restTemplate.postForObject(BASE_URL, entity, OrquestadorResponse.class);

        if (response == null || response.getRespuesta() == null) {
            return "Disculpa, tuve un problema para procesar tu mensaje. ¿Puedes escribirme de nuevo?";
        }

        return response.getRespuesta();
    }
}