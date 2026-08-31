package com.consultorio.orquestadoria.skill;

import com.consultorio.orquestadoria.client.dto.PacienteDTO;
import org.springframework.stereotype.Component;

/**
 * Centraliza la decision de que datos de nuestros microservicios internos
 * pueden viajar hacia el LLM (Anthropic) dentro del texto de las tool_result.
 * Principio: el LLM solo debe ver lo que necesita para conversar naturalmente,
 * nunca datos de contacto/identificacion que no aportan a la conversacion.
 */
@Component
public class AiDataSanitizer {

    public String describirPacienteParaIa(PacienteDTO paciente) {
        return "pacienteId=" + paciente.getId()
                + ", nombre=" + paciente.getNombreCompleto()
                + ", parentesco=" + (paciente.getParentesco() != null ? paciente.getParentesco() : "Titular");
        // Deliberadamente NO incluimos: telefono, documentoIdentidad
    }
}