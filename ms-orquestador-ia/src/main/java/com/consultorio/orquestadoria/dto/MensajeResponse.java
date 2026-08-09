package com.consultorio.orquestadoria.dto;

public class MensajeResponse {

    private String respuesta;

    public MensajeResponse(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuesta() { return respuesta; }
}