package com.consultorio.orquestadoria.client;

import com.consultorio.orquestadoria.client.dto.PacienteDTO;
import com.consultorio.orquestadoria.client.dto.RegistroRapidoRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
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
        RegistroRapidoRequest body = new RegistroRapidoRequest();
        body.setTelefono(telefono);
        body.setNombre(nombre);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegistroRapidoRequest> entity = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(BASE_URL + "/registro-rapido", entity, PacienteDTO.class);
    }

    public List<PacienteDTO> listarPorTelefono(String telefono) {
        PacienteDTO[] resultado = restTemplate.getForObject(BASE_URL + "/telefono/" + telefono + "/todos", PacienteDTO[].class);
        return resultado != null ? Arrays.asList(resultado) : List.of();
    }

    public PacienteDTO registrarFamiliar(String telefono, String nombre, String parentesco) {
        RegistroRapidoRequest body = new RegistroRapidoRequest();
        body.setTelefono(telefono);
        body.setNombre(nombre);
        body.setParentesco(parentesco);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegistroRapidoRequest> entity = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(BASE_URL + "/familiar", entity, PacienteDTO.class);
    }
}