package com.consultorio.orquestadoria.client;

import com.consultorio.orquestadoria.client.dto.PacienteDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class PacientesClient {

    private static final String BASE_URL = "http://ms-pacientes/api/pacientes";

    private final RestTemplate restTemplate;

    public PacientesClient(@Qualifier("restTemplateInterno") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<PacienteDTO> buscarPorTelefono(String telefono) {
        try {
            PacienteDTO paciente = restTemplate.getForObject(BASE_URL + "/telefono/" + telefono, PacienteDTO.class);
            return Optional.ofNullable(paciente);
        } catch (HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        }
    }

    public PacienteDTO registroRapido(String telefono, String nombre) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/registro-rapido")
                .queryParam("telefono", telefono)
                .queryParam("nombre", nombre)
                .build()
                .encode()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.postForObject(url, entity, PacienteDTO.class);
    }
}