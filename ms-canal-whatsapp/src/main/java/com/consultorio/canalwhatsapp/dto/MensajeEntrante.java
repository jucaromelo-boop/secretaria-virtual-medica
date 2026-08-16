package com.consultorio.canalwhatsapp.dto;

public class MensajeEntrante {

    private String numeroTelefono;
    private String texto;
    private String nombrePerfil;
    private String numeroDestino;

    public MensajeEntrante(String numeroTelefono, String texto, String nombrePerfil, String numeroDestino) {
        this.numeroTelefono = numeroTelefono;
        this.texto = texto;
        this.nombrePerfil = nombrePerfil;
        this.numeroDestino = numeroDestino;
    }

    public String getNumeroTelefono() { return numeroTelefono; }
    public String getTexto() { return texto; }
    public String getNombrePerfil() { return nombrePerfil; }
    public String getNumeroDestino() { return numeroDestino; }
}