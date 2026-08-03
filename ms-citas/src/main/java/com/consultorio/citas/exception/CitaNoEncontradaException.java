package com.consultorio.citas.exception;

public class CitaNoEncontradaException extends RuntimeException {
    public CitaNoEncontradaException(Long id) {
        super("No se encontro la cita con id: " + id);
    }
}