package com.consultorio.citas.repository;

import com.consultorio.citas.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    Page<Auditoria> findByOrganizacionId(Long organizacionId, Pageable pageable);
}