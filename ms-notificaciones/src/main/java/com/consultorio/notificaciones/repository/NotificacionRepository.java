package com.consultorio.notificaciones.repository;

import com.consultorio.notificaciones.model.Notificacion;
import com.consultorio.notificaciones.model.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByMedicoId(Long medicoId);
    List<Notificacion> findByCitaId(Long citaId);
    boolean existsByCitaIdAndTipo(Long citaId, TipoNotificacion tipo);
}