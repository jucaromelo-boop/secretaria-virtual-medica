package com.consultorio.medicos.controller;

import com.consultorio.medicos.dto.MedicoPerfilRequest;
import com.consultorio.medicos.dto.MedicoRequest;
import com.consultorio.medicos.dto.MedicoResponse;
import com.consultorio.medicos.model.Medico;
import com.consultorio.medicos.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN','CLINIC_ADMIN')")
    public ResponseEntity<MedicoResponse> crear(@Valid @RequestBody MedicoRequest request,
                                                org.springframework.security.core.Authentication authentication) {
        Long organizacionId = extraerOrganizacionId(authentication);
        Medico medico = medicoService.crearMedico(
                request.getNombreCompleto(),
                request.getCedulaProfesional(),
                request.getEspecialidadPrincipalId(),
                organizacionId,
                request.getUniversidad(),
                request.getAnioGraduacion());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MedicoResponse(medico));
    }

    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PATIENT','SERVICE','PLATFORM_ADMIN')")
    @GetMapping
    public List<MedicoResponse> listar(org.springframework.security.core.Authentication authentication) {
        if (esPlatformAdmin(authentication) || esService(authentication)) {
            return medicoService.listarActivos().stream()
                    .map(MedicoResponse::new)
                    .collect(Collectors.toList());
        }

        Long organizacionId = extraerOrganizacionId(authentication);
        return medicoService.listarActivosPorOrganizacion(organizacionId).stream()
                .map(MedicoResponse::new)
                .collect(Collectors.toList());
    }

    private boolean esService(org.springframework.security.core.Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.toString().equals("ROLE_SERVICE"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PATIENT','SERVICE')")
    public MedicoResponse buscarPorId(@PathVariable("id") Long id) {
        return new MedicoResponse(medicoService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PATIENT','SERVICE')")
    public List<MedicoResponse> buscarPorNombre(@RequestParam("nombre") String nombre) {
        return medicoService.buscarPorNombre(nombre).stream()
                .map(MedicoResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/especialidad/{nombre}")
    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PATIENT','SERVICE')")
    public List<MedicoResponse> buscarPorEspecialidad(@PathVariable("nombre") String nombre) {
        return medicoService.buscarPorEspecialidad(nombre).stream()
                .map(MedicoResponse::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/perfil")
    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN')")
    public MedicoResponse actualizarPerfil(@PathVariable("id") Long id, @Valid @RequestBody MedicoPerfilRequest request) {
        Medico medico = medicoService.actualizarPerfil(
                id, request.getBiografia(), request.getFotoUrl(),
                request.getIdiomas(), request.getTelefonoPersonal(), request.getEmail());
        return new MedicoResponse(medico);
    }

    @PostMapping("/{id}/especialidades/{especialidadId}")
    @PreAuthorize("hasAnyRole('CLINIC_ADMIN')")
    public MedicoResponse agregarEspecialidadSecundaria(@PathVariable("id") Long id, @PathVariable("especialidadId") Long especialidadId) {
        return new MedicoResponse(medicoService.agregarEspecialidadSecundaria(id, especialidadId));
    }

    @PostMapping("/{id}/seguros/{seguroId}")
    @PreAuthorize("hasAnyRole('CLINIC_ADMIN')")
    public MedicoResponse agregarSeguroAceptado(@PathVariable("id") Long id, @PathVariable("seguroId") Long seguroId) {
        return new MedicoResponse(medicoService.agregarSeguroAceptado(id, seguroId));
    }

    @PutMapping("/{id}/verificar")
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN')")
    public MedicoResponse verificar(@PathVariable("id") Long id) {
        return new MedicoResponse(medicoService.verificarMedico(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLINIC_ADMIN')")
    public ResponseEntity<Void> desactivar(@PathVariable("id") Long id) {
        medicoService.desactivarMedico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/telefono/{telefono}")
    @PreAuthorize("hasAnyRole('SERVICE')")
    public ResponseEntity<MedicoResponse> buscarPorTelefono(@PathVariable("telefono") String telefono) {
        return medicoService.buscarPorTelefonoPersonal(telefono)
                .map(m -> ResponseEntity.ok(new MedicoResponse(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/telefono")
    @PreAuthorize("hasAnyRole('CLINIC_ADMIN','DOCTOR')")
    public MedicoResponse actualizarTelefono(@PathVariable("id") Long id, @RequestParam("telefono") String telefono) {
        Medico medico = medicoService.buscarPorId(id);
        medico.setTelefonoPersonal(telefono);
        return new MedicoResponse(medicoService.actualizarPerfil(id, medico.getBiografia(), medico.getFotoUrl(),
                medico.getIdiomas(), telefono, medico.getEmail()));
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PLATFORM_ADMIN')")
    public ResponseEntity<org.springframework.data.domain.Page<MedicoResponse>> listarPaginado(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "nombreCompleto") String sort) {

        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page, size, org.springframework.data.domain.Sort.by(sort));

        org.springframework.data.domain.Page<MedicoResponse> resultado = medicoService.listarPaginado(pageable)
                .map(MedicoResponse::new);

        return ResponseEntity.ok(resultado);
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
}