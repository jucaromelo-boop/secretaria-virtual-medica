package com.consultorio.orquestadoria.client.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class ListaEsperaDTO {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private Set<DayOfWeek> diasPreferidos;
    private LocalTime horaInicioPreferida;
    private LocalTime horaFinPreferida;
    private LocalDate fechaLimite;
    private String estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}