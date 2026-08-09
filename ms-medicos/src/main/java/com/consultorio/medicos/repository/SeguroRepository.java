package com.consultorio.medicos.repository;

import com.consultorio.medicos.model.Seguro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeguroRepository extends JpaRepository<Seguro, Long> {
    Optional<Seguro> findByNombre(String nombre);
    List<Seguro> findByActivoTrue();
    boolean existsByNombre(String nombre);
}