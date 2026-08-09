package com.consultorio.citas.service;

import com.consultorio.citas.config.CitasProperties;
import com.consultorio.citas.exception.CancelacionNoPermitidaException;
import com.consultorio.citas.exception.CitaNoEncontradaException;
import com.consultorio.citas.exception.ConflictoHorarioException;
import com.consultorio.citas.model.Cita;
import com.consultorio.citas.model.EstadoCita;
import com.consultorio.citas.repository.CitaRepository;
import org.springframework.stereotype.Service;

import com.consultorio.citas.client.PacienteClient;
import com.consultorio.citas.dto.PacienteDTO;
import com.consultorio.citas.exception.PacienteNoValidoException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.consultorio.citas.client.MedicoClient;
import com.consultorio.citas.dto.MedicoDTO;
import com.consultorio.citas.exception.MedicoNoValidoException;
import com.consultorio.citas.event.CitaEventPublisher;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final CitasProperties citasProperties;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;
    private final CitaEventPublisher citaEventPublisher;

    public CitaService(CitaRepository citaRepository, CitasProperties citasProperties,
                       PacienteClient pacienteClient, MedicoClient medicoClient,
                       CitaEventPublisher citaEventPublisher) {
        this.citaRepository = citaRepository;
        this.citasProperties = citasProperties;
        this.pacienteClient = pacienteClient;
        this.medicoClient = medicoClient;
        this.citaEventPublisher = citaEventPublisher;
    }

    public Cita crearCita(Long pacienteId, Long medicoId, LocalDateTime fechaHora, Integer duracionMinutos) {
        PacienteDTO paciente = pacienteClient.obtenerPaciente(pacienteId);
        if (!paciente.isActivo()) {
            throw new PacienteNoValidoException(pacienteId);
        }

        MedicoDTO medico = medicoClient.obtenerMedico(medicoId);
        if (!medico.isActivo()) {
            throw new MedicoNoValidoException(medicoId);
        }

        int duracion = duracionMinutos != null ? duracionMinutos : citasProperties.getDuracionMinutosDefault();

        validarDisponibilidad(medicoId, fechaHora, duracion);

        Cita cita = new Cita(pacienteId, medicoId, fechaHora, duracion);
        Cita citaGuardada = citaRepository.save(cita);

        citaEventPublisher.publicarCitaCreada(citaGuardada.getId(), pacienteId, medicoId, fechaHora);

        return citaGuardada;
    }

    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new CitaNoEncontradaException(id));
    }

    public List<Cita> listarPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }

    public List<Cita> buscarDisponibilidad(Long medicoId, LocalDateTime desde, LocalDateTime hasta) {
        return citaRepository.findByMedicoIdAndFechaHoraBetween(medicoId, desde, hasta);
    }

    public Cita cancelarCita(Long id, String motivo) {
        Cita cita = buscarPorId(id);

        long horasHastaLaCita = ChronoUnit.HOURS.between(LocalDateTime.now(), cita.getFechaHora());
        if (horasHastaLaCita < citasProperties.getHorasMinimasAnticipacionCancelacion()) {
            throw new CancelacionNoPermitidaException(
                    "No se puede cancelar con menos de " + citasProperties.getHorasMinimasAnticipacionCancelacion()
                            + " horas de anticipacion");
        }

        cita.setEstado(EstadoCita.CANCELADA);
        Cita citaCancelada = citaRepository.save(cita);

        citaEventPublisher.publicarCitaCancelada(
                citaCancelada.getId(), citaCancelada.getPacienteId(), citaCancelada.getMedicoId(),
                citaCancelada.getFechaHora(), motivo);

        return citaCancelada;
    }

    private void validarDisponibilidad(Long medicoId, LocalDateTime fechaHora, int duracionMinutos) {
        LocalDateTime finPropuesto = fechaHora.plusMinutes(duracionMinutos + citasProperties.getBufferEntreCitasMinutos());
        LocalDateTime inicioPropuesto = fechaHora.minusMinutes(citasProperties.getBufferEntreCitasMinutos());

        List<Cita> citasDelMedico = citaRepository.findByMedicoIdAndFechaHoraBetween(
                medicoId, inicioPropuesto, finPropuesto);

        boolean hayConflicto = citasDelMedico.stream()
                .anyMatch(c -> c.getEstado() != EstadoCita.CANCELADA);

        if (hayConflicto) {
            throw new ConflictoHorarioException(
                    "El medico con id " + medicoId + " ya tiene una cita en ese horario (considerando el buffer)");
        }
    }
}