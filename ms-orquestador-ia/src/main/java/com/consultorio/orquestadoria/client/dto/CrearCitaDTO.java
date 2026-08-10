package com.consultorio.orquestadoria.client.dto;

public class CrearCitaDTO {
    private Long pacienteId;
    private Long medicoId;
    private String fechaHora;
    private Integer duracionMinutos;

    public CrearCitaDTO(Long pacienteId, Long medicoId, String fechaHora, Integer duracionMinutos) {
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.fechaHora = fechaHora;
        this.duracionMinutos = duracionMinutos;
    }

    public Long getPacienteId() { return pacienteId; }
    public Long getMedicoId() { return medicoId; }
    public String getFechaHora() { return fechaHora; }
    public Integer getDuracionMinutos() { return duracionMinutos; }
}