package com.consultorio.citas.repository;

import com.consultorio.citas.model.Cita;
import com.consultorio.citas.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByMedicoIdAndFechaHoraBetween(Long medicoId, LocalDateTime desde, LocalDateTime hasta);

    List<Cita> findByPacienteId(Long pacienteId);

    List<Cita> findByEstado(EstadoCita estado);

    List<Cita> findByFechaHoraBetweenAndEstado(LocalDateTime desde, LocalDateTime hasta, EstadoCita estado);

    List<Cita> findByOrganizacionId(Long organizacionId);
    Optional<Cita> findByIdAndOrganizacionId(Long id, Long organizacionId);
}