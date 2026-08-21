package com.consultorio.medicos.dto;

import com.consultorio.medicos.model.Organizacion;

public class OrganizacionResponse {
    private Long id;
    private String nombre;
    private String codigoIdentificador;
    private boolean activo;

    public OrganizacionResponse(Organizacion o) {
        this.id = o.getId();
        this.nombre = o.getNombre();
        this.codigoIdentificador = o.getCodigoIdentificador();
        this.activo = o.isActivo();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCodigoIdentificador() { return codigoIdentificador; }
    public boolean isActivo() { return activo; }
}