package com.consultorio.orquestadoria.client;

import com.consultorio.orquestadoria.client.dto.EspecialidadDTO;
import com.consultorio.orquestadoria.client.dto.MedicoDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.consultorio.orquestadoria.client.dto.ConsultorioDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public List<ConsultorioDTO> buscarConsultoriosPorMedico(Long medicoId) {
        ConsultorioDTO[] resultado = restTemplate.getForObject(
                "http://ms-medicos/api/consultorios/medico/" + medicoId, ConsultorioDTO[].class);
        return resultado != null ? Arrays.asList(resultado) : List.of();
    }

    public Optional<ConsultorioDTO> buscarConsultorioPorNumeroWhatsapp(String numeroWhatsapp) {
        try {
            ConsultorioDTO consultorio = restTemplate.getForObject(
                    BASE_URL + "/consultorios/whatsapp/" + numeroWhatsapp, ConsultorioDTO.class);
            return Optional.ofNullable(consultorio);
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        }
    }

    public Optional<MedicoDTO> buscarPorTelefono(String telefono) {
        try {
            MedicoDTO medico = restTemplate.getForObject(BASE_URL + "/medicos/telefono/" + telefono, MedicoDTO.class);
            return Optional.ofNullable(medico);
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        }
    }
}