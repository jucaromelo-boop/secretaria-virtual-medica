package com.consultorio.medicos.repository;

import com.consultorio.medicos.model.DiaSemana;
import com.consultorio.medicos.model.HorarioAtencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, Long> {
    List<HorarioAtencion> findByConsultorioIdAndActivoTrue(Long consultorioId);
    List<HorarioAtencion> findByConsultorioIdAndDiaSemanaAndActivoTrue(Long consultorioId, DiaSemana diaSemana);
}