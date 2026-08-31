package com.consultorio.notificaciones.repository;

import com.consultorio.notificaciones.model.Auditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    Page<Auditoria> findByOrganizacionId(Long organizacionId, Pageable pageable);
}