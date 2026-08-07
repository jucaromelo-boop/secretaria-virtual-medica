package com.consultorio.pacientes.exception;

public class DocumentoDuplicadoException extends RuntimeException {
    public DocumentoDuplicadoException(String documentoIdentidad) {
        super("Ya existe un paciente registrado con el documento: " + documentoIdentidad);
    }
}