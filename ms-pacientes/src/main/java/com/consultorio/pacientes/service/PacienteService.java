package com.consultorio.pacientes.service;

import com.consultorio.pacientes.exception.DocumentoDuplicadoException;
import com.consultorio.pacientes.exception.PacienteNoEncontradoException;
import com.consultorio.pacientes.model.Paciente;
import com.consultorio.pacientes.model.TipoDocumento;
import com.consultorio.pacientes.repository.PacienteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    // --- Pacientes dependientes / titular por telefono ---

    public List<Paciente> listarPorTelefono(String telefono) {
        return pacienteRepository.findByTelefono(telefono);
    }

    public Optional<Paciente> buscarTitularPorTelefono(String telefono) {
        return pacienteRepository.findByTelefono(telefono).stream()
                .filter(p -> p.getParentesco() == null || p.getParentesco().equalsIgnoreCase("Titular"))
                .findFirst();
    }

    public Paciente registroRapido(String telefono, String nombreCompleto) {
        return registrarConParentesco(telefono, nombreCompleto, "Titular");
    }

    public Paciente registrarFamiliar(String telefono, String nombreCompleto, String parentesco) {
        return registrarConParentesco(telefono, nombreCompleto, parentesco);
    }

    private Paciente registrarConParentesco(String telefono, String nombreCompleto, String parentesco) {
        Optional<Paciente> existente = pacienteRepository.findByTelefono(telefono).stream()
                .filter(p -> p.getNombreCompleto().equalsIgnoreCase(nombreCompleto))
                .findFirst();
        if (existente.isPresent()) {
            return existente.get();
        }

        String documentoTemporal = "WA-" + telefono + "-" + nombreCompleto.replaceAll("\\s+", "").toUpperCase();

        try {
            Paciente paciente = new Paciente(nombreCompleto, documentoTemporal, TipoDocumento.OTRO);
            paciente.setTelefono(telefono);
            paciente.setParentesco(parentesco);
            return pacienteRepository.save(paciente);
        } catch (DataIntegrityViolationException ex) {
            return pacienteRepository.findByTelefono(telefono).stream()
                    .filter(p -> p.getNombreCompleto().equalsIgnoreCase(nombreCompleto))
                    .findFirst()
                    .orElseThrow(() -> ex);
        }
    }
}