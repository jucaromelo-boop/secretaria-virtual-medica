package com.consultorio.notificaciones.client;

import com.consultorio.notificaciones.client.dto.PacienteDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PacientesClient {

    private final RestTemplate restTemplate;

    public PacientesClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PacienteDTO obtenerPaciente(Long pacienteId) {
        return restTemplate.getForObject("http://ms-pacientes/api/pacientes/" + pacienteId, PacienteDTO.class);
    }
}