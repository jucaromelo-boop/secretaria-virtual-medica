package com.consultorio.canalwhatsapp.service;

import com.consultorio.canalwhatsapp.client.OrquestadorClient;
import com.consultorio.canalwhatsapp.dto.MensajeEntrante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MensajeService {

    private static final Logger log = LoggerFactory.getLogger(MensajeService.class);

    private final OrquestadorClient orquestadorClient;

    public MensajeService(OrquestadorClient orquestadorClient) {
        this.orquestadorClient = orquestadorClient;
    }

    public String procesarMensaje(MensajeEntrante mensaje) {
        log.info("Mensaje recibido de {} ({}): {}",
                mensaje.getNombrePerfil(), mensaje.getNumeroTelefono(), mensaje.getTexto());

        return orquestadorClient.obtenerRespuesta(mensaje.getTexto(), mensaje.getNumeroTelefono(), mensaje.getNumeroDestino());
    }
}