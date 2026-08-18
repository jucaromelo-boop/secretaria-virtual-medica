package com.consultorio.citas.client;

import com.consultorio.citas.dto.PacienteDTO;
import com.consultorio.citas.exception.PacienteNoValidoException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class PacienteClient {

    private static final Logger log = LoggerFactory.getLogger(PacienteClient.class);
    private static final String BASE_URL = "http://ms-pacientes/api/pacientes";

    private final RestTemplate restTemplate;

    public PacienteClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "pacientesService", fallbackMethod = "obtenerPacienteFallback")
    public PacienteDTO obtenerPaciente(Long pacienteId) {
        try {
            return restTemplate.getForObject(BASE_URL + "/" + pacienteId, PacienteDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new PacienteNoValidoException(pacienteId);
        }
    }

    private PacienteDTO obtenerPacienteFallback(Long pacienteId, Throwable t) {
        log.error("Circuit breaker activado para ms-pacientes, pacienteId={}. Causa: {}", pacienteId, t.getMessage());
        throw new PacienteNoValidoException(pacienteId);
    }
}