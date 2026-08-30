package com.consultorio.orquestadoria.service;

import com.consultorio.orquestadoria.client.ClaudeClient;
import com.consultorio.orquestadoria.client.MedicosClient;
import com.consultorio.orquestadoria.client.dto.ClaudeMessage;
import com.consultorio.orquestadoria.client.dto.ConsultorioDTO;
import com.consultorio.orquestadoria.client.dto.MedicoDTO;
import com.consultorio.orquestadoria.config.PersonalidadConfig;
import com.consultorio.orquestadoria.memoria.MemoriaConversacionService;
import com.consultorio.orquestadoria.skill.CatalogoSkills;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class OrquestadorService {

    private final ClaudeClient claudeClient;
    private final PersonalidadConfig personalidadConfig;
    private final MemoriaConversacionService memoriaConversacionService;
    private final CatalogoSkills catalogoSkills;
    private final MedicosClient medicosClient;

    public OrquestadorService(ClaudeClient claudeClient, PersonalidadConfig personalidadConfig,
                              MemoriaConversacionService memoriaConversacionService,
                              CatalogoSkills catalogoSkills, MedicosClient medicosClient) {
        this.claudeClient = claudeClient;
        this.personalidadConfig = personalidadConfig;
        this.memoriaConversacionService = memoriaConversacionService;
        this.catalogoSkills = catalogoSkills;
        this.medicosClient = medicosClient;
    }

    public String responder(String mensajeUsuario, String numeroTelefono, String numeroDestino) {
        String clave = numeroTelefono != null ? numeroTelefono : "anonimo";

        memoriaConversacionService.agregarMensaje(clave, "user", mensajeUsuario);

        List<ClaudeMessage> historial = memoriaConversacionService.obtenerHistorial(clave);

        LocalDate hoy = LocalDate.now();
        String fechaFormateada = hoy.format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES")));

        StringBuilder contexto = new StringBuilder();
        contexto.append("\n\nDATOS DE CONTEXTO (no se los preguntes, ya los tienes):");
        contexto.append("\n- El numero de telefono de esta conversacion es: ").append(clave);
        contexto.append("\n- La fecha de hoy es: ").append(fechaFormateada).append(" (").append(hoy).append(")");
        contexto.append("\n- Usa esta fecha para calcular dias relativos como 'el jueves', 'manana', 'el proximo lunes', etc.");
        contexto.append("\n- Si ya identificaste o registraste al paciente en esta conversacion, no vuelvas a llamar "
                + "identificar_o_registrar_paciente para el mismo dato. Usa el pacienteId que ya obtuviste.");

        Long organizacionId = 1L; // default de respaldo si no se logra determinar

        Optional<MedicoDTO> medico = medicosClient.buscarPorTelefono(clave);
        boolean esMedico = medico.isPresent();

        if (esMedico) {
            MedicoDTO m = medico.get();
            contexto.append("\n\nMODO MEDICO ACTIVADO: quien te escribe es el Dr(a). ").append(m.getNombreCompleto())
                    .append(", medicoId=").append(m.getId())
                    .append(". NO es un paciente. Puedes usar las herramientas consultar_agenda_del_dia, "
                            + "reagendar_cita y cancelar_cita_como_medico. Nunca uses identificar_o_registrar_paciente "
                            + "ni crear_cita con este numero, porque no es un paciente. Tratalo con respeto profesional, "
                            + "como colega, manteniendo un tono calido pero mas directo y eficiente que con un paciente.");
        } else if (numeroDestino != null) {
            Optional<ConsultorioDTO> consultorio = medicosClient.buscarConsultorioPorNumeroWhatsapp(numeroDestino);
            if (consultorio.isPresent()) {
                ConsultorioDTO c = consultorio.get();
                organizacionId = c.getOrganizacionId();
                contexto.append("\n- IMPORTANTE: esta conversacion es del consultorio '").append(c.getNombreConsultorio())
                        .append("', consultorioId=").append(c.getId())
                        .append(", medicoId=").append(c.getMedicoId())
                        .append(", tarifaConsulta=$").append(c.getTarifaConsulta())
                        .append(", duracionConsultaMinutos=").append(c.getDuracionConsultaMinutos())
                        .append(". Ya sabes con que medico esta hablando el paciente, NO le preguntes que especialidad "
                                + "necesita ni busques otros medicos. Ve directo a identificar al paciente y agendar.");
            }
        }

        String systemPrompt = personalidadConfig.obtenerSystemPrompt() + contexto;

        String respuesta = claudeClient.enviarMensaje(systemPrompt, historial, catalogoSkills.obtenerTools(), clave, organizacionId);

        memoriaConversacionService.agregarMensaje(clave, "assistant", respuesta);

        return respuesta;
    }
}