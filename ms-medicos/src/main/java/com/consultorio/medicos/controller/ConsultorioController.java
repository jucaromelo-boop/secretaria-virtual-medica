package com.consultorio.medicos.controller;

import com.consultorio.medicos.dto.ConsultorioRequest;
import com.consultorio.medicos.dto.ConsultorioResponse;
import com.consultorio.medicos.service.ConsultorioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/consultorios")
public class ConsultorioController {

    private final ConsultorioService consultorioService;

    public ConsultorioController(ConsultorioService consultorioService) {
        this.consultorioService = consultorioService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLINIC_ADMIN')")
    public ResponseEntity<ConsultorioResponse> crear(@Valid @RequestBody ConsultorioRequest request) {
        var consultorio = consultorioService.crearConsultorio(
                request.getMedicoId(), request.getNombreConsultorio(), request.getDireccion(),
                request.getCiudad(), request.getTarifaConsulta(), request.getDuracionConsultaMinutos());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ConsultorioResponse(consultorio));
    }

    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PATIENT','SERVICE','PLATFORM_ADMIN')")
    @GetMapping("/medico/{medicoId}")
    public List<ConsultorioResponse> listarPorMedico(@PathVariable("medicoId") Long medicoId, Authentication authentication) {
        if (esPlatformAdmin(authentication) || esService(authentication)) {
            return consultorioService.listarPorMedico(medicoId).stream()
                    .map(ConsultorioResponse::new).collect(Collectors.toList());
        }
        Long organizacionId = extraerOrganizacionId(authentication);
        return consultorioService.listarPorMedicoYOrganizacion(medicoId, organizacionId).stream()
                .map(ConsultorioResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/ciudad/{ciudad}")
    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PATIENT','SERVICE')")
    public List<ConsultorioResponse> buscarPorCiudad(@PathVariable("ciudad") String ciudad) {
        return consultorioService.buscarPorCiudad(ciudad).stream()
                .map(ConsultorioResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PATIENT','SERVICE')")
    public ConsultorioResponse buscarPorId(@PathVariable("id") Long id) {
        return new ConsultorioResponse(consultorioService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLINIC_ADMIN')")
    public ResponseEntity<Void> desactivar(@PathVariable("id") Long id) {
        consultorioService.desactivarConsultorio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/whatsapp/{numero}")
    @PreAuthorize("hasAnyRole('SERVICE')")
    public ResponseEntity<ConsultorioResponse> buscarPorNumeroWhatsapp(@PathVariable("numero") String numero) {
        return consultorioService.buscarPorNumeroWhatsapp(numero)
                .map(c -> ResponseEntity.ok(new ConsultorioResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/whatsapp")
    @PreAuthorize("hasAnyRole('CLINIC_ADMIN')")
    public ConsultorioResponse asignarNumeroWhatsapp(@PathVariable("id") Long id, @RequestParam("numero") String numero) {
        return new ConsultorioResponse(consultorioService.asignarNumeroWhatsapp(id, numero));
    }

    private boolean esPlatformAdmin(org.springframework.security.core.Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.toString().equals("ROLE_PLATFORM_ADMIN"));
    }

    private Long extraerOrganizacionId(org.springframework.security.core.Authentication authentication) {
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            String claim = jwt.getClaimAsString("organizacion_id");
            if (claim != null) {
                return Long.valueOf(claim);
            }
        }
        // Si es el token de SERVICE (ms-orquestador-ia), no tiene organizacion_id propio,
        // por ahora permitimos que el rol SERVICE vea todo (ajustamos esto mas adelante
        // pasando el organizacionId explicitamente desde el orquestador)
        throw new org.springframework.security.access.AccessDeniedException(
                "No se pudo determinar la organizacion del usuario");
    }

    private boolean esService(org.springframework.security.core.Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.toString().equals("ROLE_SERVICE"));
    }
}