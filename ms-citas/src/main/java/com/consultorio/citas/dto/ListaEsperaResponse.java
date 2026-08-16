package com.consultorio.citas.dto;

import com.consultorio.citas.model.EstadoListaEspera;
import com.consultorio.citas.model.ListaEspera;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public class ListaEsperaResponse {

    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private Set<DayOfWeek> diasPreferidos;
    private LocalTime horaInicioPreferida;
    private LocalTime horaFinPreferida;
    private LocalDate fechaLimite;
    private EstadoListaEspera estado;
    private LocalDateTime fechaRegistro;

    public ListaEsperaResponse(ListaEspera l) {
        this.id = l.getId();
        this.pacienteId = l.getPacienteId();
        this.medicoId = l.getMedicoId();
        this.diasPreferidos = l.getDiasPreferidos();
        this.horaInicioPreferida = l.getHoraInicioPreferida();
        this.horaFinPreferida = l.getHoraFinPreferida();
        this.fechaLimite = l.getFechaLimite();
        this.estado = l.getEstado();
        this.fechaRegistro = l.getFechaRegistro();
    }

    public Long getId() { return id; }
    public Long getPacienteId() { return pacienteId; }
    public Long getMedicoId() { return medicoId; }
    public Set<DayOfWeek> getDiasPreferidos() { return diasPreferidos; }
    public LocalTime getHoraInicioPreferida() { return horaInicioPreferida; }
    public LocalTime getHoraFinPreferida() { return horaFinPreferida; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public EstadoListaEspera getEstado() { return estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}