package com.consultorio.notificaciones.service;

import com.consultorio.notificaciones.client.CanalWhatsappClient;
import com.consultorio.notificaciones.client.PacientesClient;
import com.consultorio.notificaciones.client.dto.CitaDTO;
import com.consultorio.notificaciones.client.dto.PacienteDTO;
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
    private final PacientesClient pacientesClient;
    private final CanalWhatsappClient canalWhatsappClient;

    public NotificacionService(NotificacionRepository notificacionRepository,
                               PacientesClient pacientesClient, CanalWhatsappClient canalWhatsappClient) {
        this.notificacionRepository = notificacionRepository;
        this.pacientesClient = pacientesClient;
        this.canalWhatsappClient = canalWhatsappClient;
    }

    public void procesarCitaCreada(Long citaId, Long pacienteId, Long medicoId, LocalDateTime fechaHora) {
        String mensaje = String.format(
                "Nueva cita programada para el %s. Paciente id: %d.", fechaHora, pacienteId);

        Notificacion notificacion = new Notificacion(citaId, pacienteId, medicoId, TipoNotificacion.CITA_CREADA, mensaje);
        enviar(notificacion);
    }

    public void procesarCitaCancelada(Long citaId, Long pacienteId, Long medicoId, LocalDateTime fechaHora, String motivo) {
        String mensajeMedico = String.format(
                "Cita del %s ha sido cancelada. Paciente id: %d. Motivo: %s",
                fechaHora, pacienteId, motivo != null ? motivo : "no especificado");

        Notificacion notificacion = new Notificacion(citaId, pacienteId, medicoId, TipoNotificacion.CITA_CANCELADA, mensajeMedico);
        enviar(notificacion);

        // Notificacion saliente por WhatsApp AL PACIENTE
        notificarPacientePorWhatsapp(pacienteId,
                "Hola, te escribimos para avisarte que tu cita del " + fechaHora
                        + " fue cancelada. Motivo: " + (motivo != null ? motivo : "no especificado")
                        + ". Si quieres, podemos ayudarte a agendar una nueva fecha, solo escribenos.");
    }

    private void notificarPacientePorWhatsapp(Long pacienteId, String mensaje) {
        try {
            PacienteDTO paciente = pacientesClient.obtenerPaciente(pacienteId);
            if (paciente != null && paciente.getTelefono() != null) {
                canalWhatsappClient.enviarMensaje(paciente.getTelefono(), mensaje);
            }
        } catch (Exception ex) {
            log.error("No se pudo notificar al paciente {} por WhatsApp: {}", pacienteId, ex.getMessage());
        }
    }

    private void enviar(Notificacion notificacion) {
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

    public void enviarRecordatorioSiCorresponde(CitaDTO cita) {
        if (notificacionRepository.existsByCitaIdAndTipo(cita.getId(), TipoNotificacion.RECORDATORIO)) {
            return;
        }

        String mensaje = "Hola, te recordamos tu cita del " + cita.getFechaHora()
                + ". Si necesitas cambiarla o cancelarla, avisanos por aqui.";

        Notificacion notificacion = new Notificacion(
                cita.getId(), cita.getPacienteId(), cita.getMedicoId(), TipoNotificacion.RECORDATORIO, mensaje);
        enviar(notificacion);

        notificarPacientePorWhatsapp(cita.getPacienteId(), mensaje);
    }
}