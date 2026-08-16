package com.consultorio.citas.model;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lista_espera")
public class ListaEspera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pacienteId;

    @Column(nullable = false)
    private Long medicoId;

    @ElementCollection
    @CollectionTable(name = "lista_espera_dias", joinColumns = @JoinColumn(name = "lista_espera_id"))
    @Column(name = "dia_semana")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> diasPreferidos = new HashSet<>();

    @Column(nullable = false)
    private LocalTime horaInicioPreferida;

    @Column(nullable = false)
    private LocalTime horaFinPreferida;

    @Column(nullable = false)
    private LocalDate fechaLimite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoListaEspera estado = EstadoListaEspera.ACTIVA;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    protected ListaEspera() {
    }

    public ListaEspera(Long pacienteId, Long medicoId, Set<DayOfWeek> diasPreferidos,
                       LocalTime horaInicioPreferida, LocalTime horaFinPreferida, LocalDate fechaLimite) {
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.diasPreferidos = diasPreferidos;
        this.horaInicioPreferida = horaInicioPreferida;
        this.horaFinPreferida = horaFinPreferida;
        this.fechaLimite = fechaLimite;
        this.estado = EstadoListaEspera.ACTIVA;
        this.fechaRegistro = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getPacienteId() { return pacienteId; }
    public Long getMedicoId() { return medicoId; }
    public Set<DayOfWeek> getDiasPreferidos() { return diasPreferidos; }
    public LocalTime getHoraInicioPreferida() { return horaInicioPreferida; }
    public LocalTime getHoraFinPreferida() { return horaFinPreferida; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public EstadoListaEspera getEstado() { return estado; }
    public void setEstado(EstadoListaEspera estado) { this.estado = estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}