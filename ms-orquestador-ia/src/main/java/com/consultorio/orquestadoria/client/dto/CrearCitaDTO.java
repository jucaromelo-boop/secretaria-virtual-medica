package com.consultorio.orquestadoria.client.dto;

public class CrearCitaDTO {
    private Long pacienteId;
    private Long medicoId;
    private String fechaHora;
    private Integer duracionMinutos;
    private String tipoConsulta;

    public CrearCitaDTO(Long pacienteId, Long medicoId, String fechaHora, Integer duracionMinutos, String tipoConsulta) {
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.fechaHora = fechaHora;
        this.duracionMinutos = duracionMinutos;
        this.tipoConsulta = tipoConsulta;
    }

    public Long getPacienteId() { return pacienteId; }
    public Long getMedicoId() { return medicoId; }
    public String getFechaHora() { return fechaHora; }
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public String getTipoConsulta() { return tipoConsulta; }
}