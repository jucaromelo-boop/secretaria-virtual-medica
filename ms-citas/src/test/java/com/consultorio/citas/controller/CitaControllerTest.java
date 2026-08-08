package com.consultorio.citas.controller;

import com.consultorio.citas.exception.CancelacionNoPermitidaException;
import com.consultorio.citas.exception.CitaNoEncontradaException;
import com.consultorio.citas.exception.ConflictoHorarioException;
import com.consultorio.citas.model.Cita;
import com.consultorio.citas.service.CitaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CitaController.class)
class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CitaService citaService;

    @Test
    void deberiaRetornar201CuandoSeCreaCitaValida() throws Exception {
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        Cita cita = new Cita(1L, "Dra. Gomez", fechaHora, 30);

        when(citaService.crearCita(anyLong(), anyString(), any(), any()))
                .thenReturn(cita);

        String body = """
                {
                  "pacienteId": 1,
                  "medicoNombre": "Dra. Gomez",
                  "fechaHora": "%s",
                  "duracionMinutos": 30
                }
                """.formatted(fechaHora.toString());

        mockMvc.perform(post("/api/citas")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pacienteId").value(1))
                .andExpect(jsonPath("$.medicoNombre").value("Dra. Gomez"));
    }

    @Test
    void deberiaRetornar400CuandoFaltaCampoObligatorio() throws Exception {
        String body = """
                {
                  "medicoNombre": "Dra. Gomez",
                  "fechaHora": "%s"
                }
                """.formatted(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/api/citas")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void deberiaRetornar409CuandoHayConflictoDeHorario() throws Exception {
        when(citaService.crearCita(anyLong(), anyString(), any(), any()))
                .thenThrow(new ConflictoHorarioException("El medico Dra. Gomez ya tiene una cita en ese horario"));

        String body = """
                {
                  "pacienteId": 1,
                  "medicoNombre": "Dra. Gomez",
                  "fechaHora": "%s",
                  "duracionMinutos": 30
                }
                """.formatted(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/api/citas")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El medico Dra. Gomez ya tiene una cita en ese horario"));
    }

    @Test
    void deberiaRetornar404CuandoLaCitaNoExiste() throws Exception {
        when(citaService.buscarPorId(99L))
                .thenThrow(new CitaNoEncontradaException(99L));

        mockMvc.perform(get("/api/citas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No se encontro la cita con id: 99"));
    }

    @Test
    void deberiaRetornar422CuandoLaCancelacionNoEstaPermitida() throws Exception {
        when(citaService.cancelarCita(any(), any()))
                .thenThrow(new CancelacionNoPermitidaException("No se puede cancelar con menos de 2 horas de anticipacion"));

        mockMvc.perform(put("/api/citas/1/cancelar")
                        .contentType("application/json")
                        .content("{\"motivo\": \"cambio de planes\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deberiaListarTodasLasCitas() throws Exception {
        Cita cita1 = new Cita(1L, "Dra. Gomez", LocalDateTime.now().plusDays(1), 30);
        Cita cita2 = new Cita(2L, "Dr. Ruiz", LocalDateTime.now().plusDays(2), 45);

        when(citaService.listarTodas()).thenReturn(List.of(cita1, cita2));

        mockMvc.perform(get("/api/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].pacienteId").value(1))
                .andExpect(jsonPath("$[1].pacienteId").value(2));
    }
}