package com.consultorio.citas.exception;

public class MedicoNoValidoException extends RuntimeException {
    public MedicoNoValidoException(Long medicoId) {
        super("No existe un medico valido con id: " + medicoId);
    }
}