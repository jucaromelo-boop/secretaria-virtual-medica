package com.consultorio.medicos.dto;

import com.consultorio.medicos.model.Seguro;

public class SeguroResponse {
    private Long id;
    private String nombre;
    private boolean activo;

    public SeguroResponse(Seguro s) {
        this.id = s.getId();
        this.nombre = s.getNombre();
        this.activo = s.isActivo();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public boolean isActivo() { return activo; }
}