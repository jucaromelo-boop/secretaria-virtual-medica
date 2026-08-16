package com.consultorio.canalwhatsapp.controller;

import com.consultorio.canalwhatsapp.dto.MensajeEntrante;
import com.consultorio.canalwhatsapp.service.MensajeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappWebhookController {

    private final MensajeService mensajeService;

    public WhatsappWebhookController(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    @PostMapping(value = "/webhook", produces = MediaType.APPLICATION_XML_VALUE)
    public String recibirMensaje(
            @RequestParam("From") String from,
            @RequestParam("Body") String body,
            @RequestParam(value = "ProfileName", required = false) String profileName,
            @RequestParam("To") String to) {

        String numeroLimpio = from.replace("whatsapp:", "");
        String numeroDestinoLimpio = to.replace("whatsapp:", "");

        MensajeEntrante mensaje = new MensajeEntrante(
                numeroLimpio, body, profileName != null ? profileName : "Paciente", numeroDestinoLimpio);

        String respuesta = mensajeService.procesarMensaje(mensaje);

        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <Response>
                <Message>%s</Message>
            </Response>
            """.formatted(respuesta);
    }
}