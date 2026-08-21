package com.consultorio.medicos.dto;

import jakarta.validation.constraints.NotBlank;

public class OrganizacionRequest {
    @NotBlank
    private String nombre;
    private String codigoIdentificador;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigoIdentificador() { return codigoIdentificador; }
    public void setCodigoIdentificador(String codigoIdentificador) { this.codigoIdentificador = codigoIdentificador; }
}