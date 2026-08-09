package com.consultorio.medicos.exception;

public class EspecialidadNoEncontradaException extends RuntimeException {
    public EspecialidadNoEncontradaException(Long id) {
        super("No se encontro la especialidad con id: " + id);
    }
}