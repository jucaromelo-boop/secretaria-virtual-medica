package com.consultorio.medicos.exception;

public class CedulaDuplicadaException extends RuntimeException {
    public CedulaDuplicadaException(String cedulaProfesional) {
        super("Ya existe un medico registrado con la cedula profesional: " + cedulaProfesional);
    }
}