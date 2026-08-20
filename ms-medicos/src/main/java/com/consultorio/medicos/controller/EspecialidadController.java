package com.consultorio.medicos.controller;

import com.consultorio.medicos.dto.EspecialidadRequest;
import com.consultorio.medicos.dto.EspecialidadResponse;
import com.consultorio.medicos.service.EspecialidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN','CLINIC_ADMIN')")
    @PostMapping
    public ResponseEntity<EspecialidadResponse> crear(@Valid @RequestBody EspecialidadRequest request) {
        var especialidad = especialidadService.crear(request.getNombre(), request.getDescripcion());
        return ResponseEntity.status(HttpStatus.CREATED).body(new EspecialidadResponse(especialidad));
    }

    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PATIENT','SERVICE')")
    @GetMapping
    public List<EspecialidadResponse> listar() {
        return especialidadService.listarActivas().stream()
                .map(EspecialidadResponse::new)
                .collect(Collectors.toList());
    }
}