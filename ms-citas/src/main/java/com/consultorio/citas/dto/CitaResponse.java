package com.consultorio.citas.dto;

import com.consultorio.citas.model.Cita;
import com.consultorio.citas.model.EstadoCita;

import java.time.LocalDateTime;

public class CitaResponse {

    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime fechaHora;
    private Integer duracionMinutos;
    private EstadoCita estado;

    public CitaResponse(Cita cita) {
        this.id = cita.getId();
        this.pacienteId = cita.getPacienteId();
        this.medicoId = cita.getMedicoId();
        this.fechaHora = cita.getFechaHora();
        this.duracionMinutos = cita.getDuracionMinutos();
        this.estado = cita.getEstado();
    }

    public Long getId() {
        return id;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public EstadoCita getEstado() {
        return estado;
    }
}