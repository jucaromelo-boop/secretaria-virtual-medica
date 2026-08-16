package com.consultorio.citas.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class ListaEsperaRequest {

    @NotNull
    private Long pacienteId;

    @NotNull
    private Long medicoId;

    @NotEmpty
    private Set<DayOfWeek> diasPreferidos;

    @NotNull
    private LocalTime horaInicioPreferida;

    @NotNull
    private LocalTime horaFinPreferida;

    @NotNull
    private LocalDate fechaLimite;

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
    public Set<DayOfWeek> getDiasPreferidos() { return diasPreferidos; }
    public void setDiasPreferidos(Set<DayOfWeek> diasPreferidos) { this.diasPreferidos = diasPreferidos; }
    public LocalTime getHoraInicioPreferida() { return horaInicioPreferida; }
    public void setHoraInicioPreferida(LocalTime horaInicioPreferida) { this.horaInicioPreferida = horaInicioPreferida; }
    public LocalTime getHoraFinPreferida() { return horaFinPreferida; }
    public void setHoraFinPreferida(LocalTime horaFinPreferida) { this.horaFinPreferida = horaFinPreferida; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }
}