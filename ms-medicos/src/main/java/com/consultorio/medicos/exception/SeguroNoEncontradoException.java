package com.consultorio.medicos.exception;

public class SeguroNoEncontradoException extends RuntimeException {
    public SeguroNoEncontradoException(Long id) {
        super("No se encontro el seguro con id: " + id);
    }
}