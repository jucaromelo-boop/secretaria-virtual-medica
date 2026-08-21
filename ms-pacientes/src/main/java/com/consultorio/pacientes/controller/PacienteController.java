package com.consultorio.pacientes.controller;

import com.consultorio.pacientes.dto.PacienteRequest;
import com.consultorio.pacientes.dto.PacienteResponse;
import com.consultorio.pacientes.dto.RegistroRapidoRequest;
import com.consultorio.pacientes.model.Paciente;
import com.consultorio.pacientes.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PreAuthorize("hasAnyRole('RECEPTIONIST','DOCTOR','CLINIC_ADMIN','SERVICE')")
    @PostMapping
    public ResponseEntity<PacienteResponse> crearPaciente(@Valid @RequestBody PacienteRequest request,
                                                          org.springframework.security.core.Authentication authentication) {
        Paciente paciente = mapearARequest(request);
        Long organizacionId = (esPlatformAdmin(authentication) || esService(authentication))
                ? 1L // organizacion default para llamadas de servicio sin contexto propio
                : extraerOrganizacionId(authentication);
        Paciente creado = pacienteService.crearPaciente(paciente, organizacionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PacienteResponse(creado));
    }

    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PLATFORM_ADMIN')")
    @GetMapping
    public List<PacienteResponse> listarActivos(org.springframework.security.core.Authentication authentication) {
        if (esPlatformAdmin(authentication)) {
            return pacienteService.listarActivos().stream().map(PacienteResponse::new).collect(Collectors.toList());
        }
        Long organizacionId = extraerOrganizacionId(authentication);
        return pacienteService.listarActivosPorOrganizacion(organizacionId).stream()
                .map(PacienteResponse::new).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PATIENT','SERVICE')")
    @GetMapping("/{id}")
    public PacienteResponse buscarPorId(@PathVariable("id") Long id) {
        return new PacienteResponse(pacienteService.buscarPorId(id));
    }

    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST')")
    @GetMapping("/documento/{documentoIdentidad}")
    public PacienteResponse buscarPorDocumento(@PathVariable("documentoIdentidad") String documentoIdentidad) {
        return new PacienteResponse(pacienteService.buscarPorDocumento(documentoIdentidad));
    }

    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST')")
    @GetMapping("/buscar")
    public List<PacienteResponse> buscarPorNombre(@RequestParam("nombre") String nombre) {
        return pacienteService.buscarPorNombre(nombre).stream()
                .map(PacienteResponse::new)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PATIENT')")
    @PutMapping("/{id}")
    public PacienteResponse actualizarPaciente(@PathVariable("id") Long id, @Valid @RequestBody PacienteRequest request) {
        Paciente datosActualizados = mapearARequest(request);
        Paciente actualizado = pacienteService.actualizarPaciente(id, datosActualizados);
        return new PacienteResponse(actualizado);
    }

    @PreAuthorize("hasAnyRole('CLINIC_ADMIN','DOCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarPaciente(@PathVariable("id") Long id) {
        pacienteService.desactivarPaciente(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('CLINIC_ADMIN','DOCTOR')")
    @PutMapping("/{id}/reactivar")
    public PacienteResponse reactivarPaciente(@PathVariable("id") Long id) {
        Paciente paciente = pacienteService.reactivarPaciente(id);
        return new PacienteResponse(paciente);
    }

    @PreAuthorize("hasAnyRole('SERVICE')")
    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<PacienteResponse> buscarPorTelefono(@PathVariable("telefono") String telefono) {
        return pacienteService.buscarTitularPorTelefono(telefono)
                .map(p -> ResponseEntity.ok(new PacienteResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('SERVICE')")
    @PostMapping("/registro-rapido")
    public ResponseEntity<PacienteResponse> registroRapido(@RequestBody RegistroRapidoRequest request) {
        Paciente paciente = pacienteService.registroRapido(request.getTelefono(), request.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(new PacienteResponse(paciente));
    }

    @PreAuthorize("hasAnyRole('SERVICE')")
    @GetMapping("/telefono/{telefono}/todos")
    public List<PacienteResponse> listarPorTelefono(@PathVariable("telefono") String telefono) {
        return pacienteService.listarPorTelefono(telefono).stream()
                .map(PacienteResponse::new)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('SERVICE')")
    @PostMapping("/familiar")
    public ResponseEntity<PacienteResponse> registrarFamiliar(@RequestBody RegistroRapidoRequest request) {
        Paciente paciente = pacienteService.registrarFamiliar(request.getTelefono(), request.getNombre(), request.getParentesco());
        return ResponseEntity.status(HttpStatus.CREATED).body(new PacienteResponse(paciente));
    }

    @PreAuthorize("hasAnyRole('DOCTOR','CLINIC_ADMIN','RECEPTIONIST','PLATFORM_ADMIN')")
    @GetMapping("/paginado")
    public ResponseEntity<org.springframework.data.domain.Page<PacienteResponse>> listarPaginado(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "nombreCompleto") String sort) {

        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page, size, org.springframework.data.domain.Sort.by(sort));

        org.springframework.data.domain.Page<PacienteResponse> resultado = pacienteService.listarPaginado(pageable)
                .map(PacienteResponse::new);

        return ResponseEntity.ok(resultado);
    }

    @PreAuthorize("hasAnyRole('CLINIC_ADMIN','RECEPTIONIST')")
    @PostMapping("/{id}/organizaciones/{organizacionId}")
    public PacienteResponse asociarAOrganizacion(@PathVariable("id") Long id, @PathVariable("organizacionId") Long organizacionId) {
        return new PacienteResponse(pacienteService.asociarAOrganizacion(id, organizacionId));
    }

    private Paciente mapearARequest(PacienteRequest request) {
        Paciente paciente = new Paciente(
                request.getNombreCompleto(),
                request.getDocumentoIdentidad(),
                request.getTipoDocumento());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setSexo(request.getSexo());
        paciente.setTelefono(request.getTelefono());
        paciente.setTelefonoAlternativo(request.getTelefonoAlternativo());
        paciente.setEmail(request.getEmail());
        paciente.setDireccion(request.getDireccion());
        paciente.setCiudad(request.getCiudad());
        paciente.setCodigoPostal(request.getCodigoPostal());
        paciente.setContactoEmergenciaNombre(request.getContactoEmergenciaNombre());
        paciente.setContactoEmergenciaTelefono(request.getContactoEmergenciaTelefono());
        paciente.setContactoEmergenciaRelacion(request.getContactoEmergenciaRelacion());
        paciente.setTipoSangre(request.getTipoSangre());
        paciente.setAlergias(request.getAlergias());
        paciente.setCondicionesCronicas(request.getCondicionesCronicas());
        paciente.setMedicamentosActuales(request.getMedicamentosActuales());
        paciente.setSeguroMedico(request.getSeguroMedico());
        paciente.setNumeroPoliza(request.getNumeroPoliza());
        paciente.setNotas(request.getNotas());
        return paciente;
    }

    private boolean esPlatformAdmin(org.springframework.security.core.Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.toString().equals("ROLE_PLATFORM_ADMIN"));
    }

    private boolean esService(org.springframework.security.core.Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.toString().equals("ROLE_SERVICE"));
    }

    private Long extraerOrganizacionId(org.springframework.security.core.Authentication authentication) {
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            String claim = jwt.getClaimAsString("organizacion_id");
            if (claim != null) {
                return Long.valueOf(claim);
            }
        }
        throw new org.springframework.security.access.AccessDeniedException(
                "No se pudo determinar la organizacion del usuario");
    }



}