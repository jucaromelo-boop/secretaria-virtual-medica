package com.consultorio.medicos.dto;

import com.consultorio.medicos.model.DiaSemana;
import com.consultorio.medicos.model.HorarioAtencion;

import java.time.LocalTime;

public class HorarioAtencionResponse {
    private Long id;
    private Long consultorioId;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean activo;

    public HorarioAtencionResponse(HorarioAtencion h) {
        this.id = h.getId();
        this.consultorioId = h.getConsultorio().getId();
        this.diaSemana = h.getDiaSemana();
        this.horaInicio = h.getHoraInicio();
        this.horaFin = h.getHoraFin();
        this.activo = h.isActivo();
    }

    public Long getId() { return id; }
    public Long getConsultorioId() { return consultorioId; }
    public DiaSemana getDiaSemana() { return diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public boolean isActivo() { return activo; }
}