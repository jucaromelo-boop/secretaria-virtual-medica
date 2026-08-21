package com.consultorio.citas.controller;

import com.consultorio.citas.config.SecurityConfig;
import com.consultorio.citas.exception.CancelacionNoPermitidaException;
import com.consultorio.citas.exception.CitaNoEncontradaException;
import com.consultorio.citas.exception.ConflictoHorarioException;
import com.consultorio.citas.model.Cita;
import com.consultorio.citas.model.TipoConsulta;
import com.consultorio.citas.service.CitaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CitaController.class)
@Import(SecurityConfig.class)
class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CitaService citaService;
    @MockBean
    private com.consultorio.citas.idempotencia.IdempotenciaCache idempotenciaCache;

    private RequestPostProcessor comoDoctor() {
        return jwt()
                .jwt(jwt -> jwt.claim("organizacion_id", "1"))
                .authorities(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_DOCTOR"));
    }

    @Test
    void deberiaRetornar201CuandoSeCreaCitaValida() throws Exception {
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        Cita cita = new Cita(1L, 1L, fechaHora, 30, TipoConsulta.PRIMERA_VEZ, 1L);

        when(citaService.crearCita(anyLong(), anyLong(), any(), any(), any()))
                .thenReturn(cita);

        String body = """
                {
                  "pacienteId": 1,
                  "medicoId": 1,
                  "fechaHora": "%s",
                  "duracionMinutos": 30,
                  "tipoConsulta": "PRIMERA_VEZ"
                }
                """.formatted(fechaHora.toString());

        mockMvc.perform(post("/api/citas")
                        .with(comoDoctor())
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pacienteId").value(1))
                .andExpect(jsonPath("$.medicoId").value(1));
    }

    @Test
    void deberiaRetornar400CuandoFaltaCampoObligatorio() throws Exception {
        String body = """
                {
                  "fechaHora": "%s"
                }
                """.formatted(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/api/citas")
                        .with(comoDoctor())
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void deberiaRetornar409CuandoHayConflictoDeHorario() throws Exception {
        when(citaService.crearCita(anyLong(), anyLong(), any(), any(), any()))
                .thenThrow(new ConflictoHorarioException("El medico ya tiene una cita en ese horario"));

        String body = """
                {
                  "pacienteId": 1,
                  "medicoId": 1,
                  "fechaHora": "%s",
                  "duracionMinutos": 30,
                  "tipoConsulta": "PRIMERA_VEZ"
                }
                """.formatted(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/api/citas")
                        .with(comoDoctor())
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El medico ya tiene una cita en ese horario"));
    }

    @Test
    void deberiaRetornar404CuandoLaCitaNoExiste() throws Exception {
        when(citaService.buscarPorIdYOrganizacion(99L, 1L))
                .thenThrow(new CitaNoEncontradaException(99L));

        mockMvc.perform(get("/api/citas/99").with(comoDoctor()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No se encontro la cita con id: 99"));
    }

    @Test
    void deberiaRetornar422CuandoLaCancelacionNoEstaPermitida() throws Exception {
        when(citaService.cancelarCita(any(), any()))
                .thenThrow(new CancelacionNoPermitidaException("No se puede cancelar con menos de 2 horas de anticipacion"));

        mockMvc.perform(put("/api/citas/1/cancelar")
                        .with(comoDoctor())
                        .contentType("application/json")
                        .content("{\"motivo\": \"cambio de planes\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deberiaListarTodasLasCitas() throws Exception {
        Cita cita1 = new Cita(1L, 1L, LocalDateTime.now().plusDays(1), 30, TipoConsulta.PRIMERA_VEZ, 1L);
        Cita cita2 = new Cita(2L, 2L, LocalDateTime.now().plusDays(2), 45, TipoConsulta.SEGUIMIENTO, 1L);

        when(citaService.listarTodasPorOrganizacion(1L)).thenReturn(List.of(cita1, cita2));

        mockMvc.perform(get("/api/citas").with(comoDoctor()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].pacienteId").value(1))
                .andExpect(jsonPath("$[1].pacienteId").value(2));
    }
}