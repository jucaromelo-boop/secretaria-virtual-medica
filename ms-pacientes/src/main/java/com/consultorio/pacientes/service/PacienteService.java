package com.consultorio.pacientes.service;

import com.consultorio.pacientes.exception.DocumentoDuplicadoException;
import com.consultorio.pacientes.exception.PacienteNoEncontradoException;
import com.consultorio.pacientes.model.Paciente;
import com.consultorio.pacientes.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente crearPaciente(Paciente paciente) {
        if (pacienteRepository.existsByDocumentoIdentidad(paciente.getDocumentoIdentidad())) {
            throw new DocumentoDuplicadoException(paciente.getDocumentoIdentidad());
        }
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarActivos() {
        return pacienteRepository.findByActivoTrue();
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));
    }

    public Paciente buscarPorDocumento(String documentoIdentidad) {
        return pacienteRepository.findByDocumentoIdentidad(documentoIdentidad)
                .orElseThrow(() -> new PacienteNoEncontradoException(documentoIdentidad));
    }

    public List<Paciente> buscarPorNombre(String nombre) {
        return pacienteRepository.findByNombreCompletoContainingIgnoreCase(nombre);
    }

    public Paciente actualizarPaciente(Long id, Paciente datosActualizados) {
        Paciente paciente = buscarPorId(id);

        paciente.setNombreCompleto(datosActualizados.getNombreCompleto());
        paciente.setTelefono(datosActualizados.getTelefono());
        paciente.setTelefonoAlternativo(datosActualizados.getTelefonoAlternativo());
        paciente.setEmail(datosActualizados.getEmail());
        paciente.setDireccion(datosActualizados.getDireccion());
        paciente.setCiudad(datosActualizados.getCiudad());
        paciente.setCodigoPostal(datosActualizados.getCodigoPostal());
        paciente.setContactoEmergenciaNombre(datosActualizados.getContactoEmergenciaNombre());
        paciente.setContactoEmergenciaTelefono(datosActualizados.getContactoEmergenciaTelefono());
        paciente.setContactoEmergenciaRelacion(datosActualizados.getContactoEmergenciaRelacion());
        paciente.setTipoSangre(datosActualizados.getTipoSangre());
        paciente.setAlergias(datosActualizados.getAlergias());
        paciente.setCondicionesCronicas(datosActualizados.getCondicionesCronicas());
        paciente.setMedicamentosActuales(datosActualizados.getMedicamentosActuales());
        paciente.setSeguroMedico(datosActualizados.getSeguroMedico());
        paciente.setNumeroPoliza(datosActualizados.getNumeroPoliza());
        paciente.setNotas(datosActualizados.getNotas());

        return pacienteRepository.save(paciente);
    }

    public void desactivarPaciente(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setActivo(false);
        pacienteRepository.save(paciente);
    }

    public Paciente reactivarPaciente(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setActivo(true);
        return pacienteRepository.save(paciente);
    }
}