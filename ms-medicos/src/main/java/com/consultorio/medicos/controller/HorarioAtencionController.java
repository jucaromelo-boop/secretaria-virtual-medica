package com.consultorio.medicos.controller;

import com.consultorio.medicos.dto.HorarioAtencionRequest;
import com.consultorio.medicos.dto.HorarioAtencionResponse;
import com.consultorio.medicos.model.DiaSemana;
import com.consultorio.medicos.service.HorarioAtencionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/horarios")
public class HorarioAtencionController {

    private final HorarioAtencionService horarioAtencionService;

    public HorarioAtencionController(HorarioAtencionService horarioAtencionService) {
        this.horarioAtencionService = horarioAtencionService;
    }

    @PostMapping
    public ResponseEntity<HorarioAtencionResponse> crear(@Valid @RequestBody HorarioAtencionRequest request) {
        var horario = horarioAtencionService.crearHorario(
                request.getConsultorioId(), request.getDiaSemana(),
                request.getHoraInicio(), request.getHoraFin());
        return ResponseEntity.status(HttpStatus.CREATED).body(new HorarioAtencionResponse(horario));
    }

    @GetMapping("/consultorio/{consultorioId}")
    public List<HorarioAtencionResponse> listarPorConsultorio(@PathVariable("consultorioId") Long consultorioId) {
        return horarioAtencionService.listarPorConsultorio(consultorioId).stream()
                .map(HorarioAtencionResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/consultorio/{consultorioId}/dia/{diaSemana}")
    public List<HorarioAtencionResponse> listarPorConsultorioYDia(
            @PathVariable("consultorioId") Long consultorioId, @PathVariable("diaSemana") DiaSemana diaSemana) {
        return horarioAtencionService.listarPorConsultorioYDia(consultorioId, diaSemana).stream()
                .map(HorarioAtencionResponse::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable("id") Long id) {
        horarioAtencionService.desactivarHorario(id);
        return ResponseEntity.noContent().build();
    }
}