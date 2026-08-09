package com.consultorio.orquestadoria.service;

import com.consultorio.orquestadoria.client.ClaudeClient;
import com.consultorio.orquestadoria.client.dto.ClaudeMessage;
import com.consultorio.orquestadoria.config.PersonalidadConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrquestadorService {

    private final ClaudeClient claudeClient;
    private final PersonalidadConfig personalidadConfig;

    public OrquestadorService(ClaudeClient claudeClient, PersonalidadConfig personalidadConfig) {
        this.claudeClient = claudeClient;
        this.personalidadConfig = personalidadConfig;
    }

    public String responder(String mensajeUsuario) {
        List<ClaudeMessage> historial = List.of(
                new ClaudeMessage("user", mensajeUsuario)
        );

        return claudeClient.enviarMensaje(personalidadConfig.obtenerSystemPrompt(), historial);
    }
}