package com.consultorio.orquestadoria.dto;

import jakarta.validation.constraints.NotBlank;

public class MensajeRequest {

    @NotBlank
    private String texto;

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}