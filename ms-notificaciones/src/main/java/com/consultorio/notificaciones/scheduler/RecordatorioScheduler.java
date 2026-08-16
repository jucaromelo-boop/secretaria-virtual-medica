package com.consultorio.notificaciones.scheduler;

import com.consultorio.notificaciones.client.CitasClient;
import com.consultorio.notificaciones.client.dto.CitaDTO;
import com.consultorio.notificaciones.service.NotificacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import com.consultorio.notificaciones.client.dto.CitaDTO;

@Component
public class RecordatorioScheduler {

    private static final Logger log = LoggerFactory.getLogger(RecordatorioScheduler.class);

    private final CitasClient citasClient;
    private final NotificacionService notificacionService;

    @Value("${recordatorios.horas-anticipacion}")
    private int horasAnticipacion;

    @Value("${recordatorios.ventana-minutos}")
    private int ventanaMinutos;

    public RecordatorioScheduler(CitasClient citasClient, NotificacionService notificacionService) {
        this.citasClient = citasClient;
        this.notificacionService = notificacionService;
    }

    @Scheduled(cron = "${recordatorios.cron}")
    public void enviarRecordatorios() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime puntoObjetivo = ahora.plusHours(horasAnticipacion);
        LocalDateTime desde = puntoObjetivo.minusMinutes(ventanaMinutos / 2);
        LocalDateTime hasta = puntoObjetivo.plusMinutes(ventanaMinutos / 2);

        log.info("Buscando citas para recordatorio entre {} y {}", desde, hasta);

        List<CitaDTO> citas = citasClient.buscarCitasEnRango(desde, hasta);

        for (CitaDTO cita : citas) {
            notificacionService.enviarRecordatorioSiCorresponde(cita);
        }

        log.info("Revision de recordatorios completada. Citas evaluadas: {}", citas.size());
    }
}