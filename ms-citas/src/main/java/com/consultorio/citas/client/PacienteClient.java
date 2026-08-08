package com.consultorio.citas.client;

import com.consultorio.citas.dto.PacienteDTO;
import com.consultorio.citas.exception.PacienteNoValidoException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class PacienteClient {

    private static final String BASE_URL = "http://ms-pacientes/api/pacientes";

    private final RestTemplate restTemplate;

    public PacienteClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PacienteDTO obtenerPaciente(Long pacienteId) {
        try {
            return restTemplate.getForObject(BASE_URL + "/" + pacienteId, PacienteDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new PacienteNoValidoException(pacienteId);
        }
    }
}