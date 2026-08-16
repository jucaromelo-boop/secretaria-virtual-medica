package com.consultorio.orquestadoria.client;

import com.consultorio.orquestadoria.client.dto.CitaDTO;
import com.consultorio.orquestadoria.client.dto.CrearCitaDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class CitasClient {

    private static final String BASE_URL = "http://ms-citas/api/citas";

    private final RestTemplate restTemplate;

    public CitasClient(@Qualifier("restTemplateInterno") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String crearCita(Long pacienteId, Long medicoId, String fechaHoraIso, Integer duracionMinutos, String tipoConsulta) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        CrearCitaDTO dto = new CrearCitaDTO(pacienteId, medicoId, fechaHoraIso, duracionMinutos, tipoConsulta);
        HttpEntity<CrearCitaDTO> entity = new HttpEntity<>(dto, headers);

        try {
            CitaDTO cita = restTemplate.postForObject(BASE_URL, entity, CitaDTO.class);
            return "Cita creada exitosamente con id " + (cita != null ? cita.getId() : "desconocido")
                    + " para el " + fechaHoraIso;
        } catch (HttpClientErrorException ex) {
            return "No se pudo crear la cita: " + ex.getResponseBodyAsString();
        }
    }

    public List<CitaDTO> listarPorPaciente(Long pacienteId) {
        CitaDTO[] resultado = restTemplate.getForObject(BASE_URL + "/paciente/" + pacienteId, CitaDTO[].class);
        return resultado != null ? Arrays.asList(resultado) : List.of();
    }

    public String cancelarCita(Long citaId, String motivo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{\"motivo\":\"" + motivo.replace("\"", "") + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(BASE_URL + "/" + citaId + "/cancelar", org.springframework.http.HttpMethod.PUT,
                    entity, CitaDTO.class);
            return "Cita " + citaId + " cancelada exitosamente";
        } catch (HttpClientErrorException ex) {
            return "No se pudo cancelar la cita: " + ex.getResponseBodyAsString();
        }
    }

    public List<CitaDTO> consultarDisponibilidad(Long medicoId, String desde, String hasta) {
        String url = BASE_URL + "/disponibilidad?medicoId=" + medicoId + "&desde=" + desde + "&hasta=" + hasta;
        CitaDTO[] resultado = restTemplate.getForObject(url, CitaDTO[].class);
        return resultado != null ? Arrays.asList(resultado) : List.of();
    }

    public String reagendarCita(Long citaId, String nuevaFechaHora) {
        // Primero obtenemos los datos de la cita original
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            CitaDTO citaOriginal = restTemplate.getForObject(BASE_URL + "/" + citaId, CitaDTO.class);
            if (citaOriginal == null) {
                return "No se encontro la cita con id " + citaId;
            }

            // Cancelamos la cita original
            String bodyCancel = "{\"motivo\":\"Reagendada por el medico\"}";
            HttpEntity<String> entityCancel = new HttpEntity<>(bodyCancel, headers);
            restTemplate.exchange(BASE_URL + "/" + citaId + "/cancelar", org.springframework.http.HttpMethod.PUT,
                    entityCancel, CitaDTO.class);

            // Creamos la nueva cita con la nueva fecha
            CrearCitaDTO nuevaDto = new CrearCitaDTO(citaOriginal.getPacienteId(), citaOriginal.getMedicoId(),
                    nuevaFechaHora, citaOriginal.getDuracionMinutos(), citaOriginal.getEstado());
            HttpEntity<CrearCitaDTO> entityCrear = new HttpEntity<>(nuevaDto, headers);
            CitaDTO nuevaCita = restTemplate.postForObject(BASE_URL, entityCrear, CitaDTO.class);

            return "Cita reagendada exitosamente. Nueva cita id=" + (nuevaCita != null ? nuevaCita.getId() : "desconocido")
                    + " para el " + nuevaFechaHora;
        } catch (HttpClientErrorException ex) {
            return "No se pudo reagendar la cita: " + ex.getResponseBodyAsString();
        }
    }
}