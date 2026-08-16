package com.consultorio.canalwhatsapp.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EnvioSalienteService {

    private static final Logger log = LoggerFactory.getLogger(EnvioSalienteService.class);

    @Value("${twilio.whatsapp-number}")
    private String numeroTwilio;

    public void enviarMensaje(String numeroDestino, String texto) {
        try {
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + numeroDestino),
                    new PhoneNumber(numeroTwilio),
                    texto
            ).create();
            log.info("Mensaje saliente enviado a {}, sid={}", numeroDestino, message.getSid());
        } catch (Exception ex) {
            log.error("Error enviando mensaje saliente a {}: {}", numeroDestino, ex.getMessage());
        }
    }
}