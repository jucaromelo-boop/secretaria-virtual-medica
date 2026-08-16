package com.consultorio.citas.controller;

import com.consultorio.citas.dto.ListaEsperaRequest;
import com.consultorio.citas.dto.ListaEsperaResponse;
import com.consultorio.citas.model.ListaEspera;
import com.consultorio.citas.service.ListaEsperaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lista-espera")
public class ListaEsperaController {

    private final ListaEsperaService listaEsperaService;

    public ListaEsperaController(ListaEsperaService listaEsperaService) {
        this.listaEsperaService = listaEsperaService;
    }

    @PostMapping
    public ResponseEntity<ListaEsperaResponse> registrar(@Valid @RequestBody ListaEsperaRequest request) {
        ListaEspera entrada = listaEsperaService.registrar(
                request.getPacienteId(), request.getMedicoId(), request.getDiasPreferidos(),
                request.getHoraInicioPreferida(), request.getHoraFinPreferida(), request.getFechaLimite());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ListaEsperaResponse(entrada));
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<ListaEsperaResponse> listarPorPaciente(@PathVariable("pacienteId") Long pacienteId) {
        return listaEsperaService.listarPorPaciente(pacienteId).stream()
                .map(ListaEsperaResponse::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable("id") Long id) {
        listaEsperaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/candidatos")
    public List<ListaEsperaResponse> buscarCandidatos(
            @RequestParam("medicoId") Long medicoId,
            @RequestParam("fechaHora") @org.springframework.format.annotation.DateTimeFormat(
                    iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora) {
        return listaEsperaService.buscarCandidatosParaHorario(medicoId, fechaHora).stream()
                .map(ListaEsperaResponse::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/ofrecida")
    public ResponseEntity<Void> marcarOfrecida(@PathVariable("id") Long id) {
        listaEsperaService.marcarOfrecida(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/ocupada")
    public ResponseEntity<Void> marcarOcupada(@PathVariable("id") Long id) {
        listaEsperaService.marcarOcupada(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ListaEsperaResponse buscarPorId(@PathVariable("id") Long id) {
        return new ListaEsperaResponse(listaEsperaService.buscarPorId(id));
    }
}