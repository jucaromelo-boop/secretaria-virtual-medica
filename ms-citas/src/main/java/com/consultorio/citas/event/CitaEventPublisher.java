package com.consultorio.citas.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CitaEventPublisher {

    private static final String TOPIC = "citas-events";

    private final KafkaTemplate<String, CitaEvent> kafkaTemplate;

    public CitaEventPublisher(KafkaTemplate<String, CitaEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicarCitaCreada(Long citaId, Long pacienteId, Long medicoId, java.time.LocalDateTime fechaHora) {
        CitaEvent evento = new CitaEvent("CITA_CREADA", citaId, pacienteId, medicoId, fechaHora, null);
        kafkaTemplate.send(TOPIC, citaId.toString(), evento);
    }

    public void publicarCitaCancelada(Long citaId, Long pacienteId, Long medicoId, java.time.LocalDateTime fechaHora, String motivo) {
        CitaEvent evento = new CitaEvent("CITA_CANCELADA", citaId, pacienteId, medicoId, fechaHora, motivo);
        kafkaTemplate.send(TOPIC, citaId.toString(), evento);
    }
}