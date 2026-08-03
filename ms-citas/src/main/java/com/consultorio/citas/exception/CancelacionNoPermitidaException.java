package com.consultorio.citas.exception;

public class CancelacionNoPermitidaException extends RuntimeException {
    public CancelacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}