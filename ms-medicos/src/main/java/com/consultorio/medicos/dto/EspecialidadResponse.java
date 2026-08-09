package com.consultorio.medicos.dto;

import com.consultorio.medicos.model.Especialidad;

public class EspecialidadResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private boolean activo;

    public EspecialidadResponse(Especialidad e) {
        this.id = e.getId();
        this.nombre = e.getNombre();
        this.descripcion = e.getDescripcion();
        this.activo = e.isActivo();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public boolean isActivo() { return activo; }
}