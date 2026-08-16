package com.consultorio.orquestadoria.skill;

import com.consultorio.orquestadoria.client.dto.ToolDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CatalogoSkills {

    public List<ToolDefinition> obtenerTools() {
        return List.of(
                new ToolDefinition(
                        "buscar_especialidades",
                        "Lista todas las especialidades medicas disponibles en el consultorio. Usa esto cuando el paciente pregunte que especialidades hay o no sepa con que tipo de medico agendar.",
                        Map.of("type", "object", "properties", Map.of(), "required", List.of())
                ),
                new ToolDefinition(
                        "buscar_medicos_por_especialidad",
                        "Busca los medicos disponibles de una especialidad especifica, incluyendo su tarifa de consulta. Menciona la tarifa cuando el paciente pregunte por precios o cuando confirmes la cita.",
                        Map.of("type", "object",
                                "properties", Map.of(
                                        "especialidad", Map.of("type", "string", "description", "Nombre de la especialidad, ej: Cardiologia")
                                ),
                                "required", List.of("especialidad"))
                ),
                new ToolDefinition(
                        "identificar_o_registrar_paciente",
                        "Busca si el paciente ya esta registrado usando su numero de telefono. Si no existe, lo registra con su nombre. Usa esto antes de agendar una cita si aun no sabes el pacienteId.",
                        Map.of("type", "object",
                                "properties", Map.of(
                                        "telefono", Map.of("type", "string", "description", "Numero de telefono del paciente"),
                                        "nombreCompleto", Map.of("type", "string", "description", "Nombre completo del paciente, requerido solo si es un paciente nuevo")
                                ),
                                "required", List.of("telefono"))
                ),
                new ToolDefinition(
                        "crear_cita",
                        "Agenda una cita medica real en el sistema. Solo usa esto cuando ya tengas confirmado el pacienteId, el medicoId, la fecha y hora exacta, y si es primera vez o seguimiento.",
                        Map.of("type", "object",
                                "properties", Map.of(
                                        "pacienteId", Map.of("type", "integer", "description", "Id del paciente, obtenido de identificar_o_registrar_paciente"),
                                        "medicoId", Map.of("type", "integer", "description", "Id del medico, obtenido de buscar_medicos_por_especialidad"),
                                        "fechaHora", Map.of("type", "string", "description", "Fecha y hora en formato ISO, ej: 2026-09-15T10:00:00"),
                                        "duracionMinutos", Map.of("type", "integer", "description", "Duracion de la cita en minutos, usa 30 si no se especifica"),
                                        "tipoConsulta", Map.of("type", "string", "description", "PRIMERA_VEZ si es la primera vez que el paciente ve a este medico, SEGUIMIENTO si ya lo ha visitado antes. Preguntale al paciente si no es obvio por el contexto.", "enum", List.of("PRIMERA_VEZ", "SEGUIMIENTO"))
                                ),
                                "required", List.of("pacienteId", "medicoId", "fechaHora", "tipoConsulta"))
                ),
                new ToolDefinition(
                        "consultar_citas_paciente",
                        "Lista las citas programadas de un paciente. Usa esto cuando el paciente quiera saber, cambiar o cancelar una cita existente.",
                        Map.of("type", "object",
                                "properties", Map.of(
                                        "pacienteId", Map.of("type", "integer", "description", "Id del paciente")
                                ),
                                "required", List.of("pacienteId"))
                ),
                new ToolDefinition(
                        "cancelar_cita",
                        "Cancela una cita existente. Solo usa esto despues de confirmar con el paciente cual cita especifica quiere cancelar.",
                        Map.of("type", "object",
                                "properties", Map.of(
                                        "citaId", Map.of("type", "integer", "description", "Id de la cita a cancelar"),
                                        "motivo", Map.of("type", "string", "description", "Motivo de la cancelacion")
                                ),
                                "required", List.of("citaId"))
                ),
                new ToolDefinition(
                        "consultar_agenda_del_dia",
                        "Muestra las citas programadas de un medico para un dia especifico. Usa esto SOLO si quien escribe es el medico (no un paciente).",
                        Map.of("type", "object",
                                "properties", Map.of(
                                        "medicoId", Map.of("type", "integer", "description", "Id del medico"),
                                        "fecha", Map.of("type", "string", "description", "Fecha en formato ISO, ej: 2026-08-20")
                                ),
                                "required", List.of("medicoId", "fecha"))
                ),
                new ToolDefinition(
                        "reagendar_cita",
                        "Cambia la fecha/hora de una cita existente. Usa esto SOLO si quien escribe es el medico. Cancela la cita anterior y crea una nueva, notificando automaticamente al paciente.",
                        Map.of("type", "object",
                                "properties", Map.of(
                                        "citaId", Map.of("type", "integer", "description", "Id de la cita a reagendar"),
                                        "nuevaFechaHora", Map.of("type", "string", "description", "Nueva fecha y hora en formato ISO")
                                ),
                                "required", List.of("citaId", "nuevaFechaHora"))
                ),
                new ToolDefinition(
                        "cancelar_cita_como_medico",
                        "Cancela una cita y notifica automaticamente al paciente. Usa esto SOLO si quien escribe es el medico.",
                        Map.of("type", "object",
                                "properties", Map.of(
                                        "citaId", Map.of("type", "integer", "description", "Id de la cita a cancelar"),
                                        "motivo", Map.of("type", "string", "description", "Motivo de la cancelacion, se le comunicara al paciente")
                                ),
                                "required", List.of("citaId", "motivo"))
                ),
                new ToolDefinition(
                        "listar_pacientes_del_telefono",
                        "Lista todas las personas (titular y familiares) registradas bajo este numero de telefono. Usa esto para preguntar para quien es la cita, o para ver quienes ya estan registrados antes de agendar.",
                        Map.of("type", "object", "properties", Map.of(), "required", List.of())
                ),
                new ToolDefinition(
                        "registrar_familiar",
                        "Registra a un familiar (hijo, esposa, padre, etc.) bajo el mismo numero de telefono del titular. Usa esto cuando el paciente quiera agendar una cita para alguien mas y esa persona aun no este registrada.",
                        Map.of("type", "object",
                                "properties", Map.of(
                                        "nombreCompleto", Map.of("type", "string", "description", "Nombre completo del familiar"),
                                        "parentesco", Map.of("type", "string", "description", "Relacion con el titular, ej: Hijo, Hija, Esposa, Esposo, Padre, Madre")
                                ),
                                "required", List.of("nombreCompleto", "parentesco"))
                )
        );
    }
}