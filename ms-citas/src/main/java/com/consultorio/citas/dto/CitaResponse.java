package com.consultorio.citas.dto;

import com.consultorio.citas.model.Cita;
import com.consultorio.citas.model.EstadoCita;

import java.time.LocalDateTime;

public class CitaResponse {

    private Long id;
    private String pacienteNombre;
    private String medicoNombre;
    private LocalDateTime fechaHora;
    private Integer duracionMinutos;
    private EstadoCita estado;

    public CitaResponse(Cita cita) {
        this.id = cita.getId();
        this.pacienteNombre = cita.getPacienteNombre();
        this.medicoNombre = cita.getMedicoNombre();
        this.fechaHora = cita.getFechaHora();
        this.duracionMinutos = cita.getDuracionMinutos();
        this.estado = cita.getEstado();
    }

    public Long getId() {
        return id;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public String getMedicoNombre() {
        return medicoNombre;
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