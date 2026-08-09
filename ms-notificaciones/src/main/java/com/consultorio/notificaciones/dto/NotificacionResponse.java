package com.consultorio.notificaciones.dto;

import com.consultorio.notificaciones.model.EstadoNotificacion;
import com.consultorio.notificaciones.model.Notificacion;
import com.consultorio.notificaciones.model.TipoNotificacion;

import java.time.LocalDateTime;

public class NotificacionResponse {

    private Long id;
    private Long citaId;
    private Long pacienteId;
    private Long medicoId;
    private TipoNotificacion tipo;
    private String mensaje;
    private EstadoNotificacion estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEnvio;

    public NotificacionResponse(Notificacion n) {
        this.id = n.getId();
        this.citaId = n.getCitaId();
        this.pacienteId = n.getPacienteId();
        this.medicoId = n.getMedicoId();
        this.tipo = n.getTipo();
        this.mensaje = n.getMensaje();
        this.estado = n.getEstado();
        this.fechaCreacion = n.getFechaCreacion();
        this.fechaEnvio = n.getFechaEnvio();
    }

    public Long getId() { return id; }
    public Long getCitaId() { return citaId; }
    public Long getPacienteId() { return pacienteId; }
    public Long getMedicoId() { return medicoId; }
    public TipoNotificacion getTipo() { return tipo; }
    public String getMensaje() { return mensaje; }
    public EstadoNotificacion getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
}