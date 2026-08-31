package com.consultorio.notificaciones.controller;

import com.consultorio.notificaciones.dto.AuditoriaResponse;
import com.consultorio.notificaciones.repository.AuditoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaController(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @PreAuthorize("hasAnyRole('CLINIC_ADMIN','PLATFORM_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<AuditoriaResponse>> listar(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").descending());
        Page<AuditoriaResponse> resultado = auditoriaRepository.findAll(pageable).map(AuditoriaResponse::new);

        return ResponseEntity.ok(resultado);
    }
}