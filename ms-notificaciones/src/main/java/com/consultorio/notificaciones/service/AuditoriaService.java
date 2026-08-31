package com.consultorio.notificaciones.service;

import com.consultorio.notificaciones.model.Auditoria;
import com.consultorio.notificaciones.repository.AuditoriaRepository;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    public void registrar(String accion, String entidadTipo, Long entidadId, Long organizacionId, String detalles) {
        String correlationId = MDC.get("correlationId");
        Auditoria registro = new Auditoria("sistema", accion, entidadTipo, entidadId, organizacionId, correlationId, detalles);
        auditoriaRepository.save(registro);
    }
}