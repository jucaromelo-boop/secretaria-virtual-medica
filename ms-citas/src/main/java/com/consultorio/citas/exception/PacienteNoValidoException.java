package com.consultorio.citas.exception;

public class PacienteNoValidoException extends RuntimeException {
    public PacienteNoValidoException(Long pacienteId) {
        super("No existe un paciente valido con id: " + pacienteId);
    }
}