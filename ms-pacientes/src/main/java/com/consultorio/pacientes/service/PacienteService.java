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
    private final AuditoriaService auditoriaService;

    public PacienteService(PacienteRepository pacienteRepository, AuditoriaService auditoriaService) {
        this.pacienteRepository = pacienteRepository;
        this.auditoriaService = auditoriaService;
    }

    public Paciente crearPaciente(Paciente paciente, Long organizacionId) {
        if (pacienteRepository.existsByDocumentoIdentidad(paciente.getDocumentoIdentidad())) {
            throw new DocumentoDuplicadoException(paciente.getDocumentoIdentidad());
        }
        paciente.getOrganizacionIds().add(organizacionId);
        Paciente creado = pacienteRepository.save(paciente);
        auditoriaService.registrar("CREAR_PACIENTE", "Paciente", creado.getId(), "Documento: " + creado.getDocumentoIdentidad());
        return creado;
    }

    public org.springframework.data.domain.Page<Paciente> listarPaginado(org.springframework.data.domain.Pageable pageable) {
        return pacienteRepository.findByActivoTrue(pageable);
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
        actualizarPaciente: auditoriaService.registrar("ACTUALIZAR_PACIENTE", "Paciente", id, "Datos actualizados");

        return pacienteRepository.save(paciente);
    }

    public void desactivarPaciente(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setActivo(false);
        pacienteRepository.save(paciente);
        auditoriaService.registrar("DESACTIVAR_PACIENTE", "Paciente", id, null);
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

    public Paciente registroRapido(String telefono, String nombreCompleto, Long organizacionId) {
        return registrarConParentesco(telefono, nombreCompleto, "Titular", organizacionId);
    }

    public Paciente registrarFamiliar(String telefono, String nombreCompleto, String parentesco, Long organizacionId) {
        return registrarConParentesco(telefono, nombreCompleto, parentesco, organizacionId);
    }

    private Paciente registrarConParentesco(String telefono, String nombreCompleto, String parentesco, Long organizacionId) {
        Optional<Paciente> existente = pacienteRepository.findByTelefono(telefono).stream()
                .filter(p -> p.getNombreCompleto().equalsIgnoreCase(nombreCompleto))
                .findFirst();
        if (existente.isPresent()) {
            Paciente paciente = existente.get();
            paciente.getOrganizacionIds().add(organizacionId);
            return pacienteRepository.save(paciente);
        }

        String documentoTemporal = "WA-" + telefono + "-" + nombreCompleto.replaceAll("\\s+", "").toUpperCase();

        try {
            Paciente paciente = new Paciente(nombreCompleto, documentoTemporal, TipoDocumento.OTRO);
            paciente.setTelefono(telefono);
            paciente.setParentesco(parentesco);
            paciente.getOrganizacionIds().add(organizacionId);
            return pacienteRepository.save(paciente);
        } catch (DataIntegrityViolationException ex) {
            return pacienteRepository.findByTelefono(telefono).stream()
                    .filter(p -> p.getNombreCompleto().equalsIgnoreCase(nombreCompleto))
                    .findFirst()
                    .orElseThrow(() -> ex);
        }
    }

    public List<Paciente> listarActivosPorOrganizacion(Long organizacionId) {
        return pacienteRepository.findByOrganizacionIdAndActivoTrue(organizacionId);
    }

    public Paciente buscarPorIdYOrganizacion(Long id, Long organizacionId) {
        return pacienteRepository.findByIdAndOrganizacionId(id, organizacionId)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));
    }

    public Paciente asociarAOrganizacion(Long pacienteId, Long organizacionId) {
        Paciente paciente = buscarPorId(pacienteId);
        paciente.getOrganizacionIds().add(organizacionId);
        return pacienteRepository.save(paciente);
    }


}