package com.consultorio.orquestadoria.client;

import com.consultorio.orquestadoria.client.dto.EspecialidadDTO;
import com.consultorio.orquestadoria.client.dto.MedicoDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class MedicosClient {

    private static final String BASE_URL = "http://ms-medicos/api";

    private final RestTemplate restTemplate;

    public MedicosClient(@Qualifier("restTemplateInterno") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<EspecialidadDTO> listarEspecialidades() {
        EspecialidadDTO[] resultado = restTemplate.getForObject(BASE_URL + "/especialidades", EspecialidadDTO[].class);
        return resultado != null ? Arrays.asList(resultado) : List.of();
    }

    public List<MedicoDTO> buscarPorEspecialidad(String nombreEspecialidad) {
        MedicoDTO[] resultado = restTemplate.getForObject(
                BASE_URL + "/medicos/especialidad/" + nombreEspecialidad, MedicoDTO[].class);
        return resultado != null ? Arrays.asList(resultado) : List.of();
    }
}