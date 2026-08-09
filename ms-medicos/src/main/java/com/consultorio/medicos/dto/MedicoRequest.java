package com.consultorio.medicos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MedicoRequest {
    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @NotBlank(message = "La cedula profesional es obligatoria")
    private String cedulaProfesional;

    @NotNull(message = "La especialidad principal es obligatoria")
    private Long especialidadPrincipalId;

    private String universidad;
    private Integer anioGraduacion;

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getCedulaProfesional() { return cedulaProfesional; }
    public void setCedulaProfesional(String cedulaProfesional) { this.cedulaProfesional = cedulaProfesional; }
    public Long getEspecialidadPrincipalId() { return especialidadPrincipalId; }
    public void setEspecialidadPrincipalId(Long especialidadPrincipalId) { this.especialidadPrincipalId = especialidadPrincipalId; }
    public String getUniversidad() { return universidad; }
    public void setUniversidad(String universidad) { this.universidad = universidad; }
    public Integer getAnioGraduacion() { return anioGraduacion; }
    public void setAnioGraduacion(Integer anioGraduacion) { this.anioGraduacion = anioGraduacion; }
}