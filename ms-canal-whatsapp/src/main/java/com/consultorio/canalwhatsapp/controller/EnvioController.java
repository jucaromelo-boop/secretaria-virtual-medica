package com.consultorio.canalwhatsapp.controller;

import com.consultorio.canalwhatsapp.dto.EnviarMensajeRequest;
import com.consultorio.canalwhatsapp.service.EnvioSalienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/whatsapp")
public class EnvioController {

    private final EnvioSalienteService envioSalienteService;

    public EnvioController(EnvioSalienteService envioSalienteService) {
        this.envioSalienteService = envioSalienteService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<Void> enviarMensaje(@Valid @RequestBody EnviarMensajeRequest request) {
        envioSalienteService.enviarMensaje(request.getNumeroDestino(), request.getMensaje());
        return ResponseEntity.accepted().build();
    }
}