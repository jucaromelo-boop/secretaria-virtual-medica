package com.consultorio.citas.service;

import com.consultorio.citas.config.CitasProperties;
import com.consultorio.citas.exception.CancelacionNoPermitidaException;
import com.consultorio.citas.exception.CitaNoEncontradaException;
import com.consultorio.citas.exception.ConflictoHorarioException;
import com.consultorio.citas.model.Cita;
import com.consultorio.citas.model.EstadoCita;
import com.consultorio.citas.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final CitasProperties citasProperties;

    public CitaService(CitaRepository citaRepository, CitasProperties citasProperties) {
        this.citaRepository = citaRepository;
        this.citasProperties = citasProperties;
    }

    public Cita crearCita(String pacienteNombre, String medicoNombre, LocalDateTime fechaHora, Integer duracionMinutos) {
        int duracion = duracionMinutos != null ? duracionMinutos : citasProperties.getDuracionMinutosDefault();

        validarDisponibilidad(medicoNombre, fechaHora, duracion);

        Cita cita = new Cita(pacienteNombre, medicoNombre, fechaHora, duracion);
        return citaRepository.save(cita);
    }

    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new CitaNoEncontradaException(id));
    }

    public List<Cita> listarPorPaciente(String pacienteNombre) {
        return citaRepository.findByPacienteNombre(pacienteNombre);
    }

    public List<Cita> buscarDisponibilidad(String medicoNombre, LocalDateTime desde, LocalDateTime hasta) {
        return citaRepository.findByMedicoNombreAndFechaHoraBetween(medicoNombre, desde, hasta);
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
        return citaRepository.save(cita);
    }

    private void validarDisponibilidad(String medicoNombre, LocalDateTime fechaHora, int duracionMinutos) {
        LocalDateTime finPropuesto = fechaHora.plusMinutes(duracionMinutos + citasProperties.getBufferEntreCitasMinutos());
        LocalDateTime inicioPropuesto = fechaHora.minusMinutes(citasProperties.getBufferEntreCitasMinutos());

        List<Cita> citasDelMedico = citaRepository.findByMedicoNombreAndFechaHoraBetween(
                medicoNombre, inicioPropuesto, finPropuesto);

        boolean hayConflicto = citasDelMedico.stream()
                .anyMatch(c -> c.getEstado() != EstadoCita.CANCELADA);

        if (hayConflicto) {
            throw new ConflictoHorarioException(
                    "El medico " + medicoNombre + " ya tiene una cita en ese horario (considerando el buffer)");
        }
    }
}