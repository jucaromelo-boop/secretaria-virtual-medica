package com.consultorio.notificaciones.controller;

import com.consultorio.notificaciones.dto.NotificacionResponse;
import com.consultorio.notificaciones.service.NotificacionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping("/medico/{medicoId}")
    public List<NotificacionResponse> listarPorMedico(@PathVariable("medicoId") Long medicoId) {
        return notificacionService.listarPorMedico(medicoId).stream()
                .map(NotificacionResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/cita/{citaId}")
    public List<NotificacionResponse> listarPorCita(@PathVariable("citaId") Long citaId) {
        return notificacionService.listarPorCita(citaId).stream()
                .map(NotificacionResponse::new)
                .collect(Collectors.toList());
    }
}