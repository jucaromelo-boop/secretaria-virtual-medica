package com.consultorio.medicos.controller;

import com.consultorio.medicos.dto.ConsultorioRequest;
import com.consultorio.medicos.dto.ConsultorioResponse;
import com.consultorio.medicos.service.ConsultorioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ConsultorioResponse> crear(@Valid @RequestBody ConsultorioRequest request) {
        var consultorio = consultorioService.crearConsultorio(
                request.getMedicoId(), request.getNombreConsultorio(), request.getDireccion(),
                request.getCiudad(), request.getTarifaConsulta(), request.getDuracionConsultaMinutos());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ConsultorioResponse(consultorio));
    }

    @GetMapping("/medico/{medicoId}")
    public List<ConsultorioResponse> listarPorMedico(@PathVariable("medicoId") Long medicoId) {
        return consultorioService.listarPorMedico(medicoId).stream()
                .map(ConsultorioResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/ciudad/{ciudad}")
    public List<ConsultorioResponse> buscarPorCiudad(@PathVariable("ciudad") String ciudad) {
        return consultorioService.buscarPorCiudad(ciudad).stream()
                .map(ConsultorioResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ConsultorioResponse buscarPorId(@PathVariable("id") Long id) {
        return new ConsultorioResponse(consultorioService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable("id") Long id) {
        consultorioService.desactivarConsultorio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/whatsapp/{numero}")
    public ResponseEntity<ConsultorioResponse> buscarPorNumeroWhatsapp(@PathVariable("numero") String numero) {
        return consultorioService.buscarPorNumeroWhatsapp(numero)
                .map(c -> ResponseEntity.ok(new ConsultorioResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/whatsapp")
    public ConsultorioResponse asignarNumeroWhatsapp(@PathVariable("id") Long id, @RequestParam("numero") String numero) {
        return new ConsultorioResponse(consultorioService.asignarNumeroWhatsapp(id, numero));
    }
}