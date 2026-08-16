package com.consultorio.medicos.dto;

import com.consultorio.medicos.model.Consultorio;

import java.math.BigDecimal;

public class ConsultorioResponse {
    private Long id;
    private Long medicoId;
    private String nombreConsultorio;
    private String direccion;
    private String ciudad;
    private String codigoPostal;
    private String telefonoConsultorio;
    private BigDecimal tarifaConsulta;
    private Integer duracionConsultaMinutos;
    private boolean activo;
    private String numeroWhatsapp;

    public ConsultorioResponse(Consultorio c) {
        this.id = c.getId();
        this.medicoId = c.getMedico().getId();
        this.nombreConsultorio = c.getNombreConsultorio();
        this.direccion = c.getDireccion();
        this.ciudad = c.getCiudad();
        this.codigoPostal = c.getCodigoPostal();
        this.telefonoConsultorio = c.getTelefonoConsultorio();
        this.tarifaConsulta = c.getTarifaConsulta();
        this.duracionConsultaMinutos = c.getDuracionConsultaMinutos();
        this.activo = c.isActivo();
        this.numeroWhatsapp = c.getNumeroWhatsapp();
    }

    public Long getId() { return id; }
    public Long getMedicoId() { return medicoId; }
    public String getNombreConsultorio() { return nombreConsultorio; }
    public String getDireccion() { return direccion; }
    public String getCiudad() { return ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
    public String getTelefonoConsultorio() { return telefonoConsultorio; }
    public BigDecimal getTarifaConsulta() { return tarifaConsulta; }
    public Integer getDuracionConsultaMinutos() { return duracionConsultaMinutos; }
    public boolean isActivo() { return activo; }
    public String getNumeroWhatsapp() { return numeroWhatsapp; }
}