package com.consultorio.orquestadoria.client.dto;

public class MedicoDTO {
    private Long id;
    private String nombreCompleto;
    private EspecialidadDTO especialidadPrincipal;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public EspecialidadDTO getEspecialidadPrincipal() { return especialidadPrincipal; }
    public void setEspecialidadPrincipal(EspecialidadDTO especialidadPrincipal) { this.especialidadPrincipal = especialidadPrincipal; }
}