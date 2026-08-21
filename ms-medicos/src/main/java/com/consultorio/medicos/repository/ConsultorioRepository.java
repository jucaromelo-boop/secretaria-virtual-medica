package com.consultorio.medicos.repository;

import com.consultorio.medicos.model.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {
    List<Consultorio> findByMedicoIdAndActivoTrue(Long medicoId);
    List<Consultorio> findByCiudadIgnoreCaseAndActivoTrue(String ciudad);
    Optional<Consultorio> findByNumeroWhatsapp(String numeroWhatsapp);
    List<Consultorio> findByMedicoIdAndOrganizacionIdAndActivoTrue(Long medicoId, Long organizacionId);
}