package com.consultorio.citas.controller;

import com.consultorio.citas.dto.CancelarCitaRequest;
import com.consultorio.citas.dto.CitaResponse;
import com.consultorio.citas.dto.CrearCitaRequest;
import com.consultorio.citas.model.Cita;
import com.consultorio.citas.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public ResponseEntity<CitaResponse> crearCita(@Valid @RequestBody CrearCitaRequest request) {
        Cita cita = citaService.crearCita(
                request.getPacienteId(),
                request.getMedicoId(),
                request.getFechaHora(),
                request.getDuracionMinutos());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CitaResponse(cita));
    }

    @GetMapping
    public List<CitaResponse> listarTodas() {
        return citaService.listarTodas().stream()
                .map(CitaResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CitaResponse buscarPorId(@PathVariable("id") Long id) {
        return new CitaResponse(citaService.buscarPorId(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<CitaResponse> listarPorPaciente(@PathVariable("pacienteId") Long pacienteId) {
        return citaService.listarPorPaciente(pacienteId).stream()
                .map(CitaResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/disponibilidad")
    public List<CitaResponse> buscarDisponibilidad(
            @RequestParam("medicoId") Long medicoId,
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return citaService.buscarDisponibilidad(medicoId, desde, hasta).stream()
                .map(CitaResponse::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/cancelar")
    public CitaResponse cancelarCita(@PathVariable("id") Long id, @RequestBody CancelarCitaRequest request) {
        Cita cita = citaService.cancelarCita(id, request.getMotivo());
        return new CitaResponse(cita);
    }
}