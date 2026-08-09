package com.consultorio.canalwhatsapp.client.dto;

public class OrquestadorRequest {

    private String texto;

    public OrquestadorRequest(String texto) {
        this.texto = texto;
    }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}