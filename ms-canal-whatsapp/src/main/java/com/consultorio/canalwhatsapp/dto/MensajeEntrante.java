package com.consultorio.canalwhatsapp.dto;

public class MensajeEntrante {

    private String numeroTelefono;
    private String texto;
    private String nombrePerfil;

    public MensajeEntrante(String numeroTelefono, String texto, String nombrePerfil) {
        this.numeroTelefono = numeroTelefono;
        this.texto = texto;
        this.nombrePerfil = nombrePerfil;
    }

    public String getNumeroTelefono() { return numeroTelefono; }
    public String getTexto() { return texto; }
    public String getNombrePerfil() { return nombrePerfil; }
}