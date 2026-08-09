package com.consultorio.notificaciones.service;

import com.consultorio.notificaciones.model.EstadoNotificacion;
import com.consultorio.notificaciones.model.Notificacion;
import com.consultorio.notificaciones.model.TipoNotificacion;
import com.consultorio.notificaciones.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public void procesarCitaCreada(Long citaId, Long pacienteId, Long medicoId, LocalDateTime fechaHora) {
        String mensaje = String.format(
                "Nueva cita programada para el %s. Paciente id: %d.", fechaHora, pacienteId);

        Notificacion notificacion = new Notificacion(citaId, pacienteId, medicoId, TipoNotificacion.CITA_CREADA, mensaje);
        enviar(notificacion);
    }

    public void procesarCitaCancelada(Long citaId, Long pacienteId, Long medicoId, LocalDateTime fechaHora, String motivo) {
        String mensaje = String.format(
                "Cita del %s ha sido cancelada. Paciente id: %d. Motivo: %s",
                fechaHora, pacienteId, motivo != null ? motivo : "no especificado");

        Notificacion notificacion = new Notificacion(citaId, pacienteId, medicoId, TipoNotificacion.CITA_CANCELADA, mensaje);
        enviar(notificacion);
    }

    private void enviar(Notificacion notificacion) {
        // Por ahora simulamos el envio con un log. Mas adelante aqui se integraria
        // el proveedor real de WhatsApp/email/SMS.
        log.info("Notificando al medico {}: {}", notificacion.getMedicoId(), notificacion.getMensaje());

        notificacion.setEstado(EstadoNotificacion.ENVIADA);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }

    public List<Notificacion> listarPorMedico(Long medicoId) {
        return notificacionRepository.findByMedicoId(medicoId);
    }

    public List<Notificacion> listarPorCita(Long citaId) {
        return notificacionRepository.findByCitaId(citaId);
    }
}