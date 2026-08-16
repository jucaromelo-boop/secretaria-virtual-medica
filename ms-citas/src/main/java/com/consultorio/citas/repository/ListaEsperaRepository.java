package com.consultorio.citas.repository;

import com.consultorio.citas.model.EstadoListaEspera;
import com.consultorio.citas.model.ListaEspera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListaEsperaRepository extends JpaRepository<ListaEspera, Long> {

    List<ListaEspera> findByMedicoIdAndEstado(Long medicoId, EstadoListaEspera estado);

    List<ListaEspera> findByPacienteIdAndEstado(Long pacienteId, EstadoListaEspera estado);
}