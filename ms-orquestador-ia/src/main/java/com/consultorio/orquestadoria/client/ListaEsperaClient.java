package com.consultorio.orquestadoria.client;

import com.consultorio.orquestadoria.client.dto.ListaEsperaDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class ListaEsperaClient {

    private static final String BASE_URL = "http://ms-citas/api/lista-espera";

    private final RestTemplate restTemplate;

    public ListaEsperaClient(@Qualifier("restTemplateInterno") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String registrar(Long pacienteId, Long medicoId, java.util.Set<java.time.DayOfWeek> diasPreferidos,
                            java.time.LocalTime horaInicio, java.time.LocalTime horaFin, java.time.LocalDate fechaLimite) {
        java.util.Map<String, Object> body = new java.util.HashMap<>();
        body.put("pacienteId", pacienteId);
        body.put("medicoId", medicoId);
        body.put("diasPreferidos", diasPreferidos);
        body.put("horaInicioPreferida", horaInicio);
        body.put("horaFinPreferida", horaFin);
        body.put("fechaLimite", fechaLimite);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<java.util.Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ListaEsperaDTO creado = restTemplate.postForObject(BASE_URL, entity, ListaEsperaDTO.class);
        return "Registrado en lista de espera con id=" + (creado != null ? creado.getId() : "desconocido");
    }

    public List<ListaEsperaDTO> listarPorPaciente(Long pacienteId) {
        ListaEsperaDTO[] resultado = restTemplate.getForObject(BASE_URL + "/paciente/" + pacienteId, ListaEsperaDTO[].class);
        return resultado != null ? Arrays.asList(resultado) : List.of();
    }
}