package com.consultorio.canalwhatsapp.service;

import com.consultorio.canalwhatsapp.dto.MensajeEntrante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MensajeService {

    private static final Logger log = LoggerFactory.getLogger(MensajeService.class);

    public String procesarMensaje(MensajeEntrante mensaje) {
        log.info("Mensaje recibido de {} ({}): {}",
                mensaje.getNombrePerfil(), mensaje.getNumeroTelefono(), mensaje.getTexto());

        // Por ahora, respuesta fija. Aqui es donde despues conectaremos
        // el orquestador de IA para generar una respuesta inteligente.
        return "Hola " + mensaje.getNombrePerfil() + ", gracias por escribir a la secretaria virtual. "
                + "Pronto podre ayudarte a agendar tu cita.";
    }
}