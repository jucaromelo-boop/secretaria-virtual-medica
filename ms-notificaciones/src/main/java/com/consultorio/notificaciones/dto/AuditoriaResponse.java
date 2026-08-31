package com.consultorio.notificaciones.dto;

import com.consultorio.notificaciones.model.Auditoria;

import java.time.LocalDateTime;

public class AuditoriaResponse {
    private Long id;
    private String usuario;
    private String accion;
    private String entidadTipo;
    private Long entidadId;
    private Long organizacionId;
    private String correlationId;
    private String detalles;
    private LocalDateTime fecha;

    public AuditoriaResponse(Auditoria a) {
        this.id = a.getId();
        this.usuario = a.getUsuario();
        this.accion = a.getAccion();
        this.entidadTipo = a.getEntidadTipo();
        this.entidadId = a.getEntidadId();
        this.organizacionId = a.getOrganizacionId();
        this.correlationId = a.getCorrelationId();
        this.detalles = a.getDetalles();
        this.fecha = a.getFecha();
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