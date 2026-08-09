package com.consultorio.medicos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ConsultorioRequest {
    @NotNull(message = "El id del medico es obligatorio")
    private Long medicoId;

    @NotBlank(message = "El nombre del consultorio es obligatorio")
    private String nombreConsultorio;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;

    private String ciudad;
    private String codigoPostal;
    private String telefonoConsultorio;

    @NotNull(message = "La tarifa de consulta es obligatoria")
    private BigDecimal tarifaConsulta;

    private Integer duracionConsultaMinutos;

    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
    public String getNombreConsultorio() { return nombreConsultorio; }
    public void setNombreConsultorio(String nombreConsultorio) { this.nombreConsultorio = nombreConsultorio; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public String getTelefonoConsultorio() { return telefonoConsultorio; }
    public void setTelefonoConsultorio(String telefonoConsultorio) { this.telefonoConsultorio = telefonoConsultorio; }
    public BigDecimal getTarifaConsulta() { return tarifaConsulta; }
    public void setTarifaConsulta(BigDecimal tarifaConsulta) { this.tarifaConsulta = tarifaConsulta; }
    public Integer getDuracionConsultaMinutos() { return duracionConsultaMinutos; }
    public void setDuracionConsultaMinutos(Integer duracionConsultaMinutos) { this.duracionConsultaMinutos = duracionConsultaMinutos; }
}