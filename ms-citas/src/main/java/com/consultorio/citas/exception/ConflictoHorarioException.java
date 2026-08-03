package com.consultorio.citas.exception;

public class ConflictoHorarioException extends RuntimeException {
    public ConflictoHorarioException(String mensaje) {
        super(mensaje);
    }
}