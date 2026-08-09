package com.consultorio.medicos.service;

import com.consultorio.medicos.exception.SeguroNoEncontradoException;
import com.consultorio.medicos.model.Seguro;
import com.consultorio.medicos.repository.SeguroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeguroService {

    private final SeguroRepository seguroRepository;

    public SeguroService(SeguroRepository seguroRepository) {
        this.seguroRepository = seguroRepository;
    }

    public Seguro crear(String nombre) {
        return seguroRepository.save(new Seguro(nombre));
    }

    public List<Seguro> listarActivos() {
        return seguroRepository.findByActivoTrue();
    }

    public Seguro buscarPorId(Long id) {
        return seguroRepository.findById(id)
                .orElseThrow(() -> new SeguroNoEncontradoException(id));
    }
}