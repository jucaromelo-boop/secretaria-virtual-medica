package com.consultorio.pacientes.controller;

import com.consultorio.pacientes.dto.AuditoriaResponse;
import com.consultorio.pacientes.service.AuditoriaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @PreAuthorize("hasAnyRole('CLINIC_ADMIN','PLATFORM_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<AuditoriaResponse>> listar(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            Authentication authentication) {

        Long organizacionId = null;
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String claim = jwt.getClaimAsString("organizacion_id");
            if (claim != null) {
                organizacionId = Long.valueOf(claim);
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").descending());
        Page<AuditoriaResponse> resultado = auditoriaService.listar(organizacionId, pageable).map(AuditoriaResponse::new);

        return ResponseEntity.ok(resultado);
    }
}