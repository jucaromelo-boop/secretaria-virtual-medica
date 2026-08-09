package com.consultorio.medicos.exception;

public class ConsultorioNoEncontradoException extends RuntimeException {
    public ConsultorioNoEncontradoException(Long id) {
        super("No se encontro el consultorio con id: " + id);
    }
}