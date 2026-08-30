package com.consultorio.medicos.service;

import com.consultorio.medicos.model.Auditoria;
import com.consultorio.medicos.repository.AuditoriaRepository;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    public void registrar(String accion, String entidadTipo, Long entidadId, String detalles) {
        String usuario = extraerUsuarioActual();
        Long organizacionId = extraerOrganizacionActual();
        String correlationId = MDC.get("correlationId");

        Auditoria registro = new Auditoria(usuario, accion, entidadTipo, entidadId, organizacionId, correlationId, detalles);
        auditoriaRepository.save(registro);
    }

    public Page<Auditoria> listar(Long organizacionId, Pageable pageable) {
        return auditoriaRepository.findByOrganizacionId(organizacionId, pageable);
    }

    private String extraerUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "desconocido";
        }
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String username = jwt.getClaimAsString("preferred_username");
            return username != null ? username : jwt.getSubject();
        }
        return authentication.getName();
    }

    private Long extraerOrganizacionActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String claim = jwt.getClaimAsString("organizacion_id");
            if (claim != null) {
                return Long.valueOf(claim);
            }
        }
        return null;
    }
}