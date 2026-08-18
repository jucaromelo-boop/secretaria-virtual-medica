package com.consultorio.medicos.service;

import com.consultorio.medicos.exception.CedulaDuplicadaException;
import com.consultorio.medicos.exception.MedicoNoEncontradoException;
import com.consultorio.medicos.model.Especialidad;
import com.consultorio.medicos.model.Medico;
import com.consultorio.medicos.model.Seguro;
import com.consultorio.medicos.repository.MedicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadService especialidadService;
    private final SeguroService seguroService;

    public MedicoService(MedicoRepository medicoRepository, EspecialidadService especialidadService, SeguroService seguroService) {
        this.medicoRepository = medicoRepository;
        this.especialidadService = especialidadService;
        this.seguroService = seguroService;
    }

    public Medico crearMedico(String nombreCompleto, String cedulaProfesional, Long especialidadPrincipalId,
                              String universidad, Integer anioGraduacion) {
        if (medicoRepository.existsByCedulaProfesional(cedulaProfesional)) {
            throw new CedulaDuplicadaException(cedulaProfesional);
        }
        Especialidad especialidad = especialidadService.buscarPorId(especialidadPrincipalId);
        Medico medico = new Medico(nombreCompleto, cedulaProfesional, especialidad);
        medico.setUniversidad(universidad);
        medico.setAnioGraduacion(anioGraduacion);
        return medicoRepository.save(medico);
    }

    public List<Medico> listarActivos() {
        return medicoRepository.findByActivoTrue();
    }

    public Medico buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new MedicoNoEncontradoException(id));
    }

    public List<Medico> buscarPorNombre(String nombre) {
        return medicoRepository.findByNombreCompletoContainingIgnoreCase(nombre);
    }

    public List<Medico> buscarPorEspecialidad(String nombreEspecialidad) {
        return medicoRepository.findByEspecialidadPrincipal_NombreIgnoreCaseAndActivoTrue(nombreEspecialidad);
    }

    public Medico actualizarPerfil(Long id, String biografia, String fotoUrl, Set<String> idiomas,
                                   String telefonoPersonal, String email) {
        Medico medico = buscarPorId(id);
        medico.setBiografia(biografia);
        medico.setFotoUrl(fotoUrl);
        medico.setIdiomas(idiomas);
        medico.setTelefonoPersonal(telefonoPersonal);
        medico.setEmail(email);
        return medicoRepository.save(medico);
    }

    public Medico agregarEspecialidadSecundaria(Long medicoId, Long especialidadId) {
        Medico medico = buscarPorId(medicoId);
        Especialidad especialidad = especialidadService.buscarPorId(especialidadId);
        medico.getEspecialidadesSecundarias().add(especialidad);
        return medicoRepository.save(medico);
    }

    public Medico agregarSeguroAceptado(Long medicoId, Long seguroId) {
        Medico medico = buscarPorId(medicoId);
        Seguro seguro = seguroService.buscarPorId(seguroId);
        medico.getSegurosAceptados().add(seguro);
        return medicoRepository.save(medico);
    }

    public Medico verificarMedico(Long id) {
        Medico medico = buscarPorId(id);
        medico.setVerificado(true);
        return medicoRepository.save(medico);
    }

    public void desactivarMedico(Long id) {
        Medico medico = buscarPorId(id);
        medico.setActivo(false);
        medicoRepository.save(medico);
    }

    public Optional<Medico> buscarPorTelefonoPersonal(String telefono) {
        return medicoRepository.findByTelefonoPersonal(telefono);
    }

    public org.springframework.data.domain.Page<Medico> listarPaginado(org.springframework.data.domain.Pageable pageable) {
        return medicoRepository.findByActivoTrue(pageable);
    }
}