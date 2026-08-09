package com.consultorio.citas.client;

import com.consultorio.citas.dto.MedicoDTO;
import com.consultorio.citas.exception.MedicoNoValidoException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class MedicoClient {

    private static final String BASE_URL = "http://ms-medicos/api/medicos";

    private final RestTemplate restTemplate;

    public MedicoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MedicoDTO obtenerMedico(Long medicoId) {
        try {
            return restTemplate.getForObject(BASE_URL + "/" + medicoId, MedicoDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new MedicoNoValidoException(medicoId);
        }
    }
}