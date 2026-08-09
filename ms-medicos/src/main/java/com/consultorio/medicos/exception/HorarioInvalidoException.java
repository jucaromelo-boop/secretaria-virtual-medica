package com.consultorio.medicos.exception;

public class HorarioInvalidoException extends RuntimeException {
    public HorarioInvalidoException(String mensaje) {
        super(mensaje);
    }
}