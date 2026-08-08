package com.consultorio.citas.repository;

import com.consultorio.citas.model.Cita;
import com.consultorio.citas.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByMedicoNombreAndFechaHoraBetween(
            String medicoNombre, LocalDateTime desde, LocalDateTime hasta);

    List<Cita> findByPacienteId(Long pacienteId);

    List<Cita> findByEstado(EstadoCita estado);
}