package com.consultorio.medicos.dto;

import com.consultorio.medicos.model.DiaSemana;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class HorarioAtencionRequest {
    @NotNull(message = "El id del consultorio es obligatorio")
    private Long consultorioId;

    @NotNull(message = "El dia de la semana es obligatorio")
    private DiaSemana diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    public Long getConsultorioId() { return consultorioId; }
    public void setConsultorioId(Long consultorioId) { this.consultorioId = consultorioId; }
    public DiaSemana getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DiaSemana diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
}