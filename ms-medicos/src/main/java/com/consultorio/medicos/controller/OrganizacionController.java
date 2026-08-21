package com.consultorio.medicos.controller;

import com.consultorio.medicos.dto.OrganizacionRequest;
import com.consultorio.medicos.dto.OrganizacionResponse;
import com.consultorio.medicos.service.OrganizacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/organizaciones")
public class OrganizacionController {

    private final OrganizacionService organizacionService;

    public OrganizacionController(OrganizacionService organizacionService) {
        this.organizacionService = organizacionService;
    }

    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN')")
    @PostMapping
    public ResponseEntity<OrganizacionResponse> crear(@Valid @RequestBody OrganizacionRequest request) {
        var org = organizacionService.crear(request.getNombre(), request.getCodigoIdentificador());
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrganizacionResponse(org));
    }

    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN')")
    @GetMapping
    public List<OrganizacionResponse> listar() {
        return organizacionService.listarTodas().stream()
                .map(OrganizacionResponse::new)
                .collect(Collectors.toList());
    }
}