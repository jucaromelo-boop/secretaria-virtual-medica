package com.consultorio.pacientes.exception;

public class PacienteNoEncontradoException extends RuntimeException {
    public PacienteNoEncontradoException(Long id) {
        super("No se encontro el paciente con id: " + id);
    }

    public PacienteNoEncontradoException(String documentoIdentidad) {
        super("No se encontro el paciente con documento: " + documentoIdentidad);
    }
}