package com.consultorio.medicos.service;

import com.consultorio.medicos.exception.ConsultorioNoEncontradoException;
import com.consultorio.medicos.model.Consultorio;
import com.consultorio.medicos.model.Medico;
import com.consultorio.medicos.repository.ConsultorioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultorioService {

    private final ConsultorioRepository consultorioRepository;
    private final MedicoService medicoService;

    public ConsultorioService(ConsultorioRepository consultorioRepository, MedicoService medicoService) {
        this.consultorioRepository = consultorioRepository;
        this.medicoService = medicoService;
    }

    public Consultorio crearConsultorio(Long medicoId, String nombreConsultorio, String direccion,
                                        String ciudad, BigDecimal tarifaConsulta, Integer duracionConsultaMinutos) {
        Medico medico = medicoService.buscarPorId(medicoId);
        Consultorio consultorio = new Consultorio(medico, nombreConsultorio, direccion, tarifaConsulta);
        consultorio.setCiudad(ciudad);
        if (duracionConsultaMinutos != null) {
            consultorio.setDuracionConsultaMinutos(duracionConsultaMinutos);
        }
        return consultorioRepository.save(consultorio);
    }

    public List<Consultorio> listarPorMedico(Long medicoId) {
        return consultorioRepository.findByMedicoIdAndActivoTrue(medicoId);
    }

    public List<Consultorio> buscarPorCiudad(String ciudad) {
        return consultorioRepository.findByCiudadIgnoreCaseAndActivoTrue(ciudad);
    }

    public Consultorio buscarPorId(Long id) {
        return consultorioRepository.findById(id)
                .orElseThrow(() -> new ConsultorioNoEncontradoException(id));
    }

    public void desactivarConsultorio(Long id) {
        Consultorio consultorio = buscarPorId(id);
        consultorio.setActivo(false);
        consultorioRepository.save(consultorio);
    }

    public Optional<Consultorio> buscarPorNumeroWhatsapp(String numeroWhatsapp) {
        return consultorioRepository.findByNumeroWhatsapp(numeroWhatsapp);
    }

    public Consultorio asignarNumeroWhatsapp(Long consultorioId, String numeroWhatsapp) {
        Consultorio consultorio = buscarPorId(consultorioId);
        consultorio.setNumeroWhatsapp(numeroWhatsapp);
        return consultorioRepository.save(consultorio);
    }

    public List<Consultorio> listarPorMedicoYOrganizacion(Long medicoId, Long organizacionId) {
        return consultorioRepository.findByMedicoIdAndOrganizacionIdAndActivoTrue(medicoId, organizacionId);
    }
}