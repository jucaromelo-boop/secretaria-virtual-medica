package com.consultorio.canalwhatsapp.dto;

import jakarta.validation.constraints.NotBlank;

public class EnviarMensajeRequest {

    @NotBlank
    private String numeroDestino;

    @NotBlank
    private String mensaje;

    public String getNumeroDestino() { return numeroDestino; }
    public void setNumeroDestino(String numeroDestino) { this.numeroDestino = numeroDestino; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}