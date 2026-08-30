package com.consultorio.citas.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private String accion;

    @Column(nullable = false)
    private String entidadTipo;

    @Column(nullable = false)
    private Long entidadId;

    private Long organizacionId;

    private String correlationId;

    @Column(length = 1000)
    private String detalles;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    protected Auditoria() {
    }

    public Auditoria(String usuario, String accion, String entidadTipo, Long entidadId,
                     Long organizacionId, String correlationId, String detalles) {
        this.usuario = usuario;
        this.accion = accion;
        this.entidadTipo = entidadTipo;
        this.entidadId = entidadId;
        this.organizacionId = organizacionId;
        this.correlationId = correlationId;
        this.detalles = detalles;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getAccion() { return accion; }
    public String getEntidadTipo() { return entidadTipo; }
    public Long getEntidadId() { return entidadId; }
    public Long getOrganizacionId() { return organizacionId; }
    public String getCorrelationId() { return correlationId; }
    public String getDetalles() { return detalles; }
    public LocalDateTime getFecha() { return fecha; }
}