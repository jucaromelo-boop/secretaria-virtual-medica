package com.consultorio.canalwhatsapp.client.dto;

public class OrquestadorRequest {

    private String texto;
    private String numeroTelefono;
    private String numeroDestino;

    public OrquestadorRequest(String texto, String numeroTelefono, String numeroDestino) {
        this.texto = texto;
        this.numeroTelefono = numeroTelefono;
        this.numeroDestino = numeroDestino;
    }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getNumeroTelefono() { return numeroTelefono; }
    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }
    public String getNumeroDestino() { return numeroDestino; }
    public void setNumeroDestino(String numeroDestino) { this.numeroDestino = numeroDestino; }
}