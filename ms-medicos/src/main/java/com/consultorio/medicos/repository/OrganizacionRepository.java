package com.consultorio.medicos.repository;

import com.consultorio.medicos.model.Organizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizacionRepository extends JpaRepository<Organizacion, Long> {
    Optional<Organizacion> findByCodigoIdentificador(String codigo);
}