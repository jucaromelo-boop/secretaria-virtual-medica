package com.consultorio.pacientes.repository;

import com.consultorio.pacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByDocumentoIdentidad(String documentoIdentidad);

    List<Paciente> findByActivoTrue();

    List<Paciente> findByNombreCompletoContainingIgnoreCase(String nombre);

    boolean existsByDocumentoIdentidad(String documentoIdentidad);

    List<Paciente> findByTelefono(String telefono);


    org.springframework.data.domain.Page<Paciente> findByActivoTrue(org.springframework.data.domain.Pageable pageable);

}