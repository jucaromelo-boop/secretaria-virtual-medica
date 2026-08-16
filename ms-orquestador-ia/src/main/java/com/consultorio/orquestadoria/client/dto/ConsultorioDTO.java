package com.consultorio.orquestadoria.client.dto;

import java.math.BigDecimal;

public class ConsultorioDTO {
    private Long id;
    private Long medicoId;
    private String nombreConsultorio;
    private BigDecimal tarifaConsulta;
    private Integer duracionConsultaMinutos;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
    public String getNombreConsultorio() { return nombreConsultorio; }
    public void setNombreConsultorio(String nombreConsultorio) { this.nombreConsultorio = nombreConsultorio; }
    public BigDecimal getTarifaConsulta() { return tarifaConsulta; }
    public void setTarifaConsulta(BigDecimal tarifaConsulta) { this.tarifaConsulta = tarifaConsulta; }
    public Integer getDuracionConsultaMinutos() { return duracionConsultaMinutos; }
    public void setDuracionConsultaMinutos(Integer duracionConsultaMinutos) { this.duracionConsultaMinutos = duracionConsultaMinutos; }
}