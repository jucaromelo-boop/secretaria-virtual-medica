package com.consultorio.medicos.exception;

public class OrganizacionNoEncontradaException extends RuntimeException {
    public OrganizacionNoEncontradaException(Long id) {
        super("No se encontro la organizacion con id: " + id);
    }
}