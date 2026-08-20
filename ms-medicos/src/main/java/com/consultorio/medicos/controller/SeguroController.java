package com.consultorio.medicos.controller;

import com.consultorio.medicos.dto.SeguroRequest;
import com.consultorio.medicos.dto.SeguroResponse;
import com.consultorio.medicos.service.SeguroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seguros")
public class SeguroController {

    private final SeguroService seguroService;

    public SeguroController(SeguroService seguroService) {
        this.seguroService = seguroService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN','CLINIC_ADMIN')")
    public ResponseEntity<SeguroResponse> crear(@Valid @RequestBody SeguroRequest request) {
        var seguro = seguroService.crear(request.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(new SeguroResponse(seguro));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PATIENT','SERVICE')")
    public List<SeguroResponse> listar() {
        return seguroService.listarActivos().stream()
                .map(SeguroResponse::new)
                .collect(Collectors.toList());
    }
}