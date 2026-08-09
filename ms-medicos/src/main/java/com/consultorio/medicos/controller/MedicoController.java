package com.consultorio.medicos.controller;

import com.consultorio.medicos.dto.MedicoPerfilRequest;
import com.consultorio.medicos.dto.MedicoRequest;
import com.consultorio.medicos.dto.MedicoResponse;
import com.consultorio.medicos.model.Medico;
import com.consultorio.medicos.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MedicoResponse> crear(@Valid @RequestBody MedicoRequest request) {
        Medico medico = medicoService.crearMedico(
                request.getNombreCompleto(),
                request.getCedulaProfesional(),
                request.getEspecialidadPrincipalId(),
                request.getUniversidad(),
                request.getAnioGraduacion());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MedicoResponse(medico));
    }

    @GetMapping
    public List<MedicoResponse> listar() {
        return medicoService.listarActivos().stream()
                .map(MedicoResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MedicoResponse buscarPorId(@PathVariable("id") Long id) {
        return new MedicoResponse(medicoService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public List<MedicoResponse> buscarPorNombre(@RequestParam("nombre") String nombre) {
        return medicoService.buscarPorNombre(nombre).stream()
                .map(MedicoResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/especialidad/{nombre}")
    public List<MedicoResponse> buscarPorEspecialidad(@PathVariable("nombre") String nombre) {
        return medicoService.buscarPorEspecialidad(nombre).stream()
                .map(MedicoResponse::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/perfil")
    public MedicoResponse actualizarPerfil(@PathVariable("id") Long id, @Valid @RequestBody MedicoPerfilRequest request) {
        Medico medico = medicoService.actualizarPerfil(
                id, request.getBiografia(), request.getFotoUrl(),
                request.getIdiomas(), request.getTelefonoPersonal(), request.getEmail());
        return new MedicoResponse(medico);
    }

    @PostMapping("/{id}/especialidades/{especialidadId}")
    public MedicoResponse agregarEspecialidadSecundaria(@PathVariable("id") Long id, @PathVariable("especialidadId") Long especialidadId) {
        return new MedicoResponse(medicoService.agregarEspecialidadSecundaria(id, especialidadId));
    }

    @PostMapping("/{id}/seguros/{seguroId}")
    public MedicoResponse agregarSeguroAceptado(@PathVariable("id") Long id, @PathVariable("seguroId") Long seguroId) {
        return new MedicoResponse(medicoService.agregarSeguroAceptado(id, seguroId));
    }

    @PutMapping("/{id}/verificar")
    public MedicoResponse verificar(@PathVariable("id") Long id) {
        return new MedicoResponse(medicoService.verificarMedico(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable("id") Long id) {
        medicoService.desactivarMedico(id);
        return ResponseEntity.noContent().build();
    }
}