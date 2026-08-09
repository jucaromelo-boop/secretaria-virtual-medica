package com.consultorio.notificaciones.event;

import com.consultorio.notificaciones.service.NotificacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CitaEventListener {

    private static final Logger log = LoggerFactory.getLogger(CitaEventListener.class);

    private final NotificacionService notificacionService;

    public CitaEventListener(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @KafkaListener(topics = "citas-events", groupId = "ms-notificaciones-group")
    public void escuchar(CitaEvent evento) {
        log.info("Evento recibido: {}", evento.getTipoEvento());

        switch (evento.getTipoEvento()) {
            case "CITA_CREADA" -> notificacionService.procesarCitaCreada(
                    evento.getCitaId(), evento.getPacienteId(), evento.getMedicoId(), evento.getFechaHora());
            case "CITA_CANCELADA" -> notificacionService.procesarCitaCancelada(
                    evento.getCitaId(), evento.getPacienteId(), evento.getMedicoId(), evento.getFechaHora(), evento.getMotivo());
            default -> log.warn("Tipo de evento desconocido: {}", evento.getTipoEvento());
        }
    }
}