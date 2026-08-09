package com.consultorio.medicos.service;

import com.consultorio.medicos.exception.EspecialidadNoEncontradaException;
import com.consultorio.medicos.model.Especialidad;
import com.consultorio.medicos.repository.EspecialidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    public Especialidad crear(String nombre, String descripcion) {
        return especialidadRepository.save(new Especialidad(nombre, descripcion));
    }

    public List<Especialidad> listarActivas() {
        return especialidadRepository.findByActivoTrue();
    }

    public Especialidad buscarPorId(Long id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new EspecialidadNoEncontradaException(id));
    }
}