package com.consultorio.orquestadoria.skill;

import com.consultorio.orquestadoria.client.CitasClient;
import com.consultorio.orquestadoria.client.MedicosClient;
import com.consultorio.orquestadoria.client.PacientesClient;
import com.consultorio.orquestadoria.client.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SkillExecutor {

    private final MedicosClient medicosClient;
    private final PacientesClient pacientesClient;
    private final CitasClient citasClient;

    public SkillExecutor(MedicosClient medicosClient, PacientesClient pacientesClient, CitasClient citasClient) {
        this.medicosClient = medicosClient;
        this.pacientesClient = pacientesClient;
        this.citasClient = citasClient;
    }

    public String ejecutar(String nombreSkill, Map<String, Object> input, String telefonoConversacion) {
        try {
            return switch (nombreSkill) {
                case "buscar_especialidades" -> buscarEspecialidades();
                case "buscar_medicos_por_especialidad" -> buscarMedicosPorEspecialidad(input);
                case "identificar_o_registrar_paciente" -> identificarORegistrarPaciente(input);
                case "crear_cita" -> crearCita(input);
                case "consultar_citas_paciente" -> consultarCitasPaciente(input);
                case "cancelar_cita" -> cancelarCita(input);
                case "consultar_agenda_del_dia" -> consultarAgendaDelDia(input);
                case "reagendar_cita" -> reagendarCita(input);
                case "cancelar_cita_como_medico" -> cancelarCitaComoMedico(input);
                case "listar_pacientes_del_telefono" -> listarPacientesDelTelefono(telefonoConversacion);
                case "registrar_familiar" -> registrarFamiliar(input, telefonoConversacion);
                default -> "Skill desconocida: " + nombreSkill;
            };
        } catch (Exception ex) {
            return "Ocurrio un error ejecutando la operacion: " + ex.getMessage();
        }
    }

    private String listarPacientesDelTelefono(String telefono) {
        List<PacienteDTO> pacientes = pacientesClient.listarPorTelefono(telefono);
        if (pacientes.isEmpty()) {
            return "No hay pacientes registrados con este numero todavia.";
        }
        return pacientes.stream()
                .map(p -> "pacienteId=" + p.getId() + ", nombre=" + p.getNombreCompleto()
                        + ", parentesco=" + (p.getParentesco() != null ? p.getParentesco() : "Titular"))
                .collect(Collectors.joining("; "));
    }

    private String registrarFamiliar(Map<String, Object> input, String telefono) {
        String nombre = (String) input.get("nombreCompleto");
        String parentesco = (String) input.get("parentesco");
        PacienteDTO creado = pacientesClient.registrarFamiliar(telefono, nombre, parentesco);
        return "Familiar registrado exitosamente: pacienteId=" + creado.getId() + ", nombre=" + creado.getNombreCompleto()
                + ", parentesco=" + creado.getParentesco();
    }

    private String buscarEspecialidades() {
        List<EspecialidadDTO> especialidades = medicosClient.listarEspecialidades();
        if (especialidades.isEmpty()) {
            return "No hay especialidades registradas actualmente.";
        }
        return especialidades.stream()
                .map(e -> e.getNombre() + (e.getDescripcion() != null ? " - " + e.getDescripcion() : ""))
                .collect(Collectors.joining("; "));
    }

    private String buscarMedicosPorEspecialidad(Map<String, Object> input) {
        String especialidad = (String) input.get("especialidad");
        List<MedicoDTO> medicos = medicosClient.buscarPorEspecialidad(especialidad);
        if (medicos.isEmpty()) {
            return "No se encontraron medicos disponibles para la especialidad " + especialidad;
        }

        return medicos.stream()
                .map(m -> {
                    String info = "medicoId=" + m.getId() + ", nombre=" + m.getNombreCompleto();
                    List<ConsultorioDTO> consultorios = medicosClient.buscarConsultoriosPorMedico(m.getId());
                    if (!consultorios.isEmpty()) {
                        ConsultorioDTO c = consultorios.get(0);
                        info += ", tarifaConsulta=$" + c.getTarifaConsulta() + ", duracionMinutos=" + c.getDuracionConsultaMinutos();
                    }
                    return info;
                })
                .collect(Collectors.joining("; "));
    }

    private String identificarORegistrarPaciente(Map<String, Object> input) {
        String telefono = (String) input.get("telefono");
        Optional<PacienteDTO> existente = pacientesClient.buscarPorTelefono(telefono);

        if (existente.isPresent()) {
            PacienteDTO p = existente.get();
            return "Paciente encontrado: pacienteId=" + p.getId() + ", nombre=" + p.getNombreCompleto();
        }

        String nombre = (String) input.get("nombreCompleto");
        if (nombre == null || nombre.isBlank()) {
            return "El paciente no esta registrado. Necesitas pedirle su nombre completo antes de continuar.";
        }

        PacienteDTO creado = pacientesClient.registroRapido(telefono, nombre);
        return "Paciente registrado exitosamente: pacienteId=" + creado.getId() + ", nombre=" + creado.getNombreCompleto();
    }

    private String crearCita(Map<String, Object> input) {
        Long pacienteId = ((Number) input.get("pacienteId")).longValue();
        Long medicoId = ((Number) input.get("medicoId")).longValue();
        String fechaHora = (String) input.get("fechaHora");
        Integer duracion = input.get("duracionMinutos") != null
                ? ((Number) input.get("duracionMinutos")).intValue() : 30;
        String tipoConsulta = (String) input.get("tipoConsulta");

        return citasClient.crearCita(pacienteId, medicoId, fechaHora, duracion, tipoConsulta);
    }

    private String consultarCitasPaciente(Map<String, Object> input) {
        Long pacienteId = ((Number) input.get("pacienteId")).longValue();
        List<CitaDTO> citas = citasClient.listarPorPaciente(pacienteId);

        if (citas.isEmpty()) {
            return "El paciente no tiene citas registradas.";
        }

        return citas.stream()
                .map(c -> "citaId=" + c.getId() + ", fecha=" + c.getFechaHora() + ", estado=" + c.getEstado())
                .collect(Collectors.joining("; "));
    }

    private String cancelarCita(Map<String, Object> input) {
        Long citaId = ((Number) input.get("citaId")).longValue();
        String motivo = input.get("motivo") != null ? (String) input.get("motivo") : "No especificado";

        return citasClient.cancelarCita(citaId, motivo);
    }

    private String consultarAgendaDelDia(Map<String, Object> input) {
        Long medicoId = ((Number) input.get("medicoId")).longValue();
        String fecha = (String) input.get("fecha");

        String desde = fecha + "T00:00:00";
        String hasta = fecha + "T23:59:59";

        List<CitaDTO> citas = citasClient.consultarDisponibilidad(medicoId, desde, hasta);

        if (citas.isEmpty()) {
            return "No hay citas programadas para ese dia.";
        }

        return citas.stream()
                .map(c -> "citaId=" + c.getId() + ", hora=" + c.getFechaHora() + ", pacienteId=" + c.getPacienteId()
                        + ", estado=" + c.getEstado())
                .collect(Collectors.joining("; "));
    }

    private String reagendarCita(Map<String, Object> input) {
        Long citaId = ((Number) input.get("citaId")).longValue();
        String nuevaFechaHora = (String) input.get("nuevaFechaHora");

        return citasClient.reagendarCita(citaId, nuevaFechaHora);
    }

    private String cancelarCitaComoMedico(Map<String, Object> input) {
        Long citaId = ((Number) input.get("citaId")).longValue();
        String motivo = (String) input.get("motivo");

        return citasClient.cancelarCita(citaId, motivo);
    }
}