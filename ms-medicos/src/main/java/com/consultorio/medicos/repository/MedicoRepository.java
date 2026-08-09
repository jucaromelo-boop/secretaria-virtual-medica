package com.consultorio.medicos.repository;

import com.consultorio.medicos.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByCedulaProfesional(String cedulaProfesional);
    List<Medico> findByActivoTrue();
    List<Medico> findByNombreCompletoContainingIgnoreCase(String nombre);
    List<Medico> findByEspecialidadPrincipal_NombreIgnoreCaseAndActivoTrue(String nombreEspecialidad);
    boolean existsByCedulaProfesional(String cedulaProfesional);
}