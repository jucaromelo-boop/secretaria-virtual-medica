package com.consultorio.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long pacienteId;

    @NotNull
    @Column(nullable = false)
    private Long medicoId;

    @NotNull
    @Future
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private Integer duracionMinutos = 30;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCita estado = EstadoCita.PROGRAMADA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConsulta tipoConsulta = TipoConsulta.PRIMERA_VEZ;

    protected Cita() {
        // constructor vacio requerido por JPA
    }

    public Cita(Long pacienteId, Long medicoId, LocalDateTime fechaHora, Integer duracionMinutos, TipoConsulta tipoConsulta) {
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.fechaHora = fechaHora;
        this.duracionMinutos = duracionMinutos;
        this.tipoConsulta = tipoConsulta != null ? tipoConsulta : TipoConsulta.PRIMERA_VEZ;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public TipoConsulta getTipoConsulta() { return tipoConsulta; }

    public void setTipoConsulta(TipoConsulta tipoConsulta) { this.tipoConsulta = tipoConsulta; }
}