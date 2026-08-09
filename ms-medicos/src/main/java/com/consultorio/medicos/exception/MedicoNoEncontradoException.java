package com.consultorio.medicos.exception;

public class MedicoNoEncontradoException extends RuntimeException {
    public MedicoNoEncontradoException(Long id) {
        super("No se encontro el medico con id: " + id);
    }
}