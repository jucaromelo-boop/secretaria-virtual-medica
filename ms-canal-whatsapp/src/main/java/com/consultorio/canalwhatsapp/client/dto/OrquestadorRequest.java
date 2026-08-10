package com.consultorio.canalwhatsapp.client.dto;

public class OrquestadorRequest {

    private String texto;
    private String numeroTelefono;

    public OrquestadorRequest(String texto, String numeroTelefono) {
        this.texto = texto;
        this.numeroTelefono = numeroTelefono;
    }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getNumeroTelefono() { return numeroTelefono; }
    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }
}