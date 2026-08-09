package com.consultorio.medicos.repository;

import com.consultorio.medicos.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    Optional<Especialidad> findByNombre(String nombre);
    List<Especialidad> findByActivoTrue();
    boolean existsByNombre(String nombre);
}