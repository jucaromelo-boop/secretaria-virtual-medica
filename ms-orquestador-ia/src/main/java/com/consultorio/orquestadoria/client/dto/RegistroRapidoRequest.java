package com.consultorio.orquestadoria.client.dto;

public class RegistroRapidoRequest {
    private String telefono;
    private String nombre;
    private String parentesco;

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getParentesco() { return parentesco; }
    public void setParentesco(String parentesco) { this.parentesco = parentesco; }
}