package com.consultorio.orquestadoria.controller;

import com.consultorio.orquestadoria.dto.MensajeRequest;
import com.consultorio.orquestadoria.dto.MensajeResponse;
import com.consultorio.orquestadoria.service.OrquestadorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orquestador")
public class OrquestadorController {

    private final OrquestadorService orquestadorService;

    public OrquestadorController(OrquestadorService orquestadorService) {
        this.orquestadorService = orquestadorService;
    }

    @PostMapping(value = "/mensaje", produces = "application/json;charset=UTF-8")
    public MensajeResponse procesarMensaje(@Valid @RequestBody MensajeRequest request) {
        String respuesta = orquestadorService.responder(request.getTexto());
        return new MensajeResponse(respuesta);
    }
}