package com.consultorio.orquestadoria.service;

import com.consultorio.orquestadoria.client.ClaudeClient;
import com.consultorio.orquestadoria.client.dto.ClaudeMessage;
import com.consultorio.orquestadoria.config.PersonalidadConfig;
import com.consultorio.orquestadoria.memoria.MemoriaConversacionService;
import com.consultorio.orquestadoria.skill.CatalogoSkills;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class OrquestadorService {

    private final ClaudeClient claudeClient;
    private final PersonalidadConfig personalidadConfig;
    private final MemoriaConversacionService memoriaConversacionService;
    private final CatalogoSkills catalogoSkills;

    public OrquestadorService(ClaudeClient claudeClient, PersonalidadConfig personalidadConfig,
                              MemoriaConversacionService memoriaConversacionService,
                              CatalogoSkills catalogoSkills) {
        this.claudeClient = claudeClient;
        this.personalidadConfig = personalidadConfig;
        this.memoriaConversacionService = memoriaConversacionService;
        this.catalogoSkills = catalogoSkills;
    }

    public String responder(String mensajeUsuario, String numeroTelefono) {
        String clave = numeroTelefono != null ? numeroTelefono : "anonimo";

        memoriaConversacionService.agregarMensaje(clave, "user", mensajeUsuario);

        List<ClaudeMessage> historial = memoriaConversacionService.obtenerHistorial(clave);

        LocalDate hoy = LocalDate.now();
        String fechaFormateada = hoy.format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES")));

        String systemPrompt = personalidadConfig.obtenerSystemPrompt()
                + "\n\nDATOS DE CONTEXTO (no se los preguntes al paciente, ya los tienes):"
                + "\n- El numero de telefono de esta conversacion es: " + clave
                + "\n- La fecha de hoy es: " + fechaFormateada + " (" + hoy + ")"
                + "\n- Usa esta fecha para calcular dias relativos como 'el jueves', 'manana', 'el proximo lunes', etc."
                + "\n- Si ya identificaste o registraste al paciente en esta conversacion, no vuelvas a llamar "
                + "identificar_o_registrar_paciente para el mismo dato. Usa el pacienteId que ya obtuviste.";

        String respuesta = claudeClient.enviarMensaje(systemPrompt, historial, catalogoSkills.obtenerTools());

        memoriaConversacionService.agregarMensaje(clave, "assistant", respuesta);

        return respuesta;
    }
}