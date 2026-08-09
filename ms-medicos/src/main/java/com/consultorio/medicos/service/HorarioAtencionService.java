package com.consultorio.medicos.service;

import com.consultorio.medicos.exception.HorarioInvalidoException;
import com.consultorio.medicos.exception.HorarioSolapadoException;
import com.consultorio.medicos.model.Consultorio;
import com.consultorio.medicos.model.DiaSemana;
import com.consultorio.medicos.model.HorarioAtencion;
import com.consultorio.medicos.repository.HorarioAtencionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class HorarioAtencionService {

    private final HorarioAtencionRepository horarioAtencionRepository;
    private final ConsultorioService consultorioService;

    public HorarioAtencionService(HorarioAtencionRepository horarioAtencionRepository, ConsultorioService consultorioService) {
        this.horarioAtencionRepository = horarioAtencionRepository;
        this.consultorioService = consultorioService;
    }

    public HorarioAtencion crearHorario(Long consultorioId, DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        if (!horaInicio.isBefore(horaFin)) {
            throw new HorarioInvalidoException("La hora de inicio debe ser anterior a la hora de fin");
        }

        Consultorio consultorio = consultorioService.buscarPorId(consultorioId);

        List<HorarioAtencion> horariosDelDia = horarioAtencionRepository
                .findByConsultorioIdAndDiaSemanaAndActivoTrue(consultorioId, diaSemana);

        boolean seTraslapa = horariosDelDia.stream().anyMatch(existente ->
                horaInicio.isBefore(existente.getHoraFin()) && horaFin.isAfter(existente.getHoraInicio()));

        if (seTraslapa) {
            throw new HorarioSolapadoException(
                    "Ya existe un horario para este consultorio el " + diaSemana + " que se traslapa con el rango indicado");
        }

        return horarioAtencionRepository.save(new HorarioAtencion(consultorio, diaSemana, horaInicio, horaFin));
    }

    public List<HorarioAtencion> listarPorConsultorio(Long consultorioId) {
        return horarioAtencionRepository.findByConsultorioIdAndActivoTrue(consultorioId);
    }

    public List<HorarioAtencion> listarPorConsultorioYDia(Long consultorioId, DiaSemana diaSemana) {
        return horarioAtencionRepository.findByConsultorioIdAndDiaSemanaAndActivoTrue(consultorioId, diaSemana);
    }

    public void desactivarHorario(Long id) {
        HorarioAtencion horario = horarioAtencionRepository.findById(id)
                .orElseThrow(() -> new HorarioInvalidoException("No se encontro el horario con id: " + id));
        horario.setActivo(false);
        horarioAtencionRepository.save(horario);
    }
}