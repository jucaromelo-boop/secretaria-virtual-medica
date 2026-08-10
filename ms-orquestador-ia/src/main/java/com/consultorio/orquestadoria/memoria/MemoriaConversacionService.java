package com.consultorio.orquestadoria.memoria;

import com.consultorio.orquestadoria.client.dto.ClaudeMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MemoriaConversacionService {

    private static final int MAX_MENSAJES_HISTORIAL = 20;

    private final Map<String, List<ClaudeMessage>> conversaciones = new ConcurrentHashMap<>();

    public List<ClaudeMessage> obtenerHistorial(String numeroTelefono) {
        return conversaciones.computeIfAbsent(numeroTelefono, k -> new CopyOnWriteArrayList<>());
    }

    public void agregarMensaje(String numeroTelefono, String role, String contenido) {
        List<ClaudeMessage> historial = obtenerHistorial(numeroTelefono);
        historial.add(new ClaudeMessage(role, contenido));

        while (historial.size() > MAX_MENSAJES_HISTORIAL) {
            historial.remove(0);
        }
    }

    public void limpiarConversacion(String numeroTelefono) {
        conversaciones.remove(numeroTelefono);
    }
}