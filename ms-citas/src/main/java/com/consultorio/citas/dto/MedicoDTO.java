package com.consultorio.citas.dto;

public class MedicoDTO {

    private Long id;
    private String nombreCompleto;
    private boolean activo;
    private Long organizacionId;


    public MedicoDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public Long getOrganizacionId() { return organizacionId; }
    public void setOrganizacionId(Long organizacionId) { this.organizacionId = organizacionId; }
}