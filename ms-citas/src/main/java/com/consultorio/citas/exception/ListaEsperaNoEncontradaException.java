package com.consultorio.citas.exception;

public class ListaEsperaNoEncontradaException extends RuntimeException {
    public ListaEsperaNoEncontradaException(Long id) {
        super("No se encontro la entrada de lista de espera con id: " + id);
    }
}