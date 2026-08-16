package com.consultorio.notificaciones.client;

import com.consultorio.notificaciones.client.dto.CitaDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class CitasClient {

    private final RestTemplate restTemplate;

    public CitasClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CitaDTO> buscarCitasEnRango(LocalDateTime desde, LocalDateTime hasta) {
        String url = "http://ms-citas/api/citas/rango?desde=" + desde + "&hasta=" + hasta;
        CitaDTO[] resultado = restTemplate.getForObject(url, CitaDTO[].class);
        return resultado != null ? Arrays.asList(resultado) : List.of();
    }
}