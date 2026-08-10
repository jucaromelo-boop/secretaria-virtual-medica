package com.consultorio.orquestadoria.dto;

import jakarta.validation.constraints.NotBlank;

public class MensajeRequest {

    @NotBlank
    private String texto;

    private String numeroTelefono;

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getNumeroTelefono() { return numeroTelefono; }
    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }
}