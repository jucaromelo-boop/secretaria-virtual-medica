package com.consultorio.citas.client;

import com.consultorio.citas.dto.MedicoDTO;
import com.consultorio.citas.exception.MedicoNoValidoException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class MedicoClient {

    private static final Logger log = LoggerFactory.getLogger(MedicoClient.class);
    private static final String BASE_URL = "http://ms-medicos/api/medicos";

    private final RestTemplate restTemplate;

    public MedicoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "medicoService", fallbackMethod = "obtenerMedicoFallback")
    public MedicoDTO obtenerMedico(Long medicoId) {
        try {
            return restTemplate.getForObject(BASE_URL + "/" + medicoId, MedicoDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new MedicoNoValidoException(medicoId);
        }
    }

    private MedicoDTO obtenerMedicoFallback(Long medicoId, Throwable t) {
        log.error("Circuit breaker activado para ms-medicos, medicoId={}. Causa: {}", medicoId, t.getMessage());
        throw new MedicoNoValidoException(medicoId);
    }
}