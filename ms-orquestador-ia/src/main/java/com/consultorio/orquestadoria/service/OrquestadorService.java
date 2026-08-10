package com.consultorio.orquestadoria.service;

import com.consultorio.orquestadoria.client.ClaudeClient;
import com.consultorio.orquestadoria.client.dto.ClaudeMessage;
import com.consultorio.orquestadoria.config.PersonalidadConfig;
import com.consultorio.orquestadoria.memoria.MemoriaConversacionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrquestadorService {

    private final ClaudeClient claudeClient;
    private final PersonalidadConfig personalidadConfig;
    private final MemoriaConversacionService memoriaConversacionService;

    public OrquestadorService(ClaudeClient claudeClient, PersonalidadConfig personalidadConfig,
                              MemoriaConversacionService memoriaConversacionService) {
        this.claudeClient = claudeClient;
        this.personalidadConfig = personalidadConfig;
        this.memoriaConversacionService = memoriaConversacionService;
    }

    public String responder(String mensajeUsuario, String numeroTelefono) {
        String clave = numeroTelefono != null ? numeroTelefono : "anonimo";

        memoriaConversacionService.agregarMensaje(clave, "user", mensajeUsuario);

        List<ClaudeMessage> historial = memoriaConversacionService.obtenerHistorial(clave);

        String respuesta = claudeClient.enviarMensaje(personalidadConfig.obtenerSystemPrompt(), historial);

        memoriaConversacionService.agregarMensaje(clave, "assistant", respuesta);

        return respuesta;
    }
}