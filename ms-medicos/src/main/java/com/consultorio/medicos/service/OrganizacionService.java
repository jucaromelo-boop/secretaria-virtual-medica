package com.consultorio.medicos.service;

import com.consultorio.medicos.exception.OrganizacionNoEncontradaException;
import com.consultorio.medicos.model.Organizacion;
import com.consultorio.medicos.repository.OrganizacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizacionService {

    private final OrganizacionRepository organizacionRepository;

    public OrganizacionService(OrganizacionRepository organizacionRepository) {
        this.organizacionRepository = organizacionRepository;
    }

    public Organizacion crear(String nombre, String codigoIdentificador) {
        return organizacionRepository.save(new Organizacion(nombre, codigoIdentificador));
    }

    public Organizacion buscarPorId(Long id) {
        return organizacionRepository.findById(id)
                .orElseThrow(() -> new OrganizacionNoEncontradaException(id));
    }

    public List<Organizacion> listarTodas() {
        return organizacionRepository.findAll();
    }
}