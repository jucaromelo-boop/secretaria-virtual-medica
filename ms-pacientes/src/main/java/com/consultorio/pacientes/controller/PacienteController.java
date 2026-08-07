package com.consultorio.pacientes.controller;

import com.consultorio.pacientes.dto.PacienteRequest;
import com.consultorio.pacientes.dto.PacienteResponse;
import com.consultorio.pacientes.model.Paciente;
import com.consultorio.pacientes.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> crearPaciente(@Valid @RequestBody PacienteRequest request) {
        Paciente paciente = mapearARequest(request);
        Paciente creado = pacienteService.crearPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PacienteResponse(creado));
    }

    @GetMapping
    public List<PacienteResponse> listarActivos() {
        return pacienteService.listarActivos().stream()
                .map(PacienteResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PacienteResponse buscarPorId(@PathVariable("id") Long id) {
        return new PacienteResponse(pacienteService.buscarPorId(id));
    }

    @GetMapping("/documento/{documentoIdentidad}")
    public PacienteResponse buscarPorDocumento(@PathVariable("documentoIdentidad") String documentoIdentidad) {
        return new PacienteResponse(pacienteService.buscarPorDocumento(documentoIdentidad));
    }

    @GetMapping("/buscar")
    public List<PacienteResponse> buscarPorNombre(@RequestParam("nombre") String nombre) {
        return pacienteService.buscarPorNombre(nombre).stream()
                .map(PacienteResponse::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public PacienteResponse actualizarPaciente(@PathVariable("id") Long id, @Valid @RequestBody PacienteRequest request) {
        Paciente datosActualizados = mapearARequest(request);
        Paciente actualizado = pacienteService.actualizarPaciente(id, datosActualizados);
        return new PacienteResponse(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarPaciente(@PathVariable("id") Long id) {
        pacienteService.desactivarPaciente(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reactivar")
    public PacienteResponse reactivarPaciente(@PathVariable("id") Long id) {
        Paciente paciente = pacienteService.reactivarPaciente(id);
        return new PacienteResponse(paciente);
    }

    private Paciente mapearARequest(PacienteRequest request) {
        Paciente paciente = new Paciente(
                request.getNombreCompleto(),
                request.getDocumentoIdentidad(),
                request.getTipoDocumento());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setSexo(request.getSexo());
        paciente.setTelefono(request.getTelefono());
        paciente.setTelefonoAlternativo(request.getTelefonoAlternativo());
        paciente.setEmail(request.getEmail());
        paciente.setDireccion(request.getDireccion());
        paciente.setCiudad(request.getCiudad());
        paciente.setCodigoPostal(request.getCodigoPostal());
        paciente.setContactoEmergenciaNombre(request.getContactoEmergenciaNombre());
        paciente.setContactoEmergenciaTelefono(request.getContactoEmergenciaTelefono());
        paciente.setContactoEmergenciaRelacion(request.getContactoEmergenciaRelacion());
        paciente.setTipoSangre(request.getTipoSangre());
        paciente.setAlergias(request.getAlergias());
        paciente.setCondicionesCronicas(request.getCondicionesCronicas());
        paciente.setMedicamentosActuales(request.getMedicamentosActuales());
        paciente.setSeguroMedico(request.getSeguroMedico());
        paciente.setNumeroPoliza(request.getNumeroPoliza());
        paciente.setNotas(request.getNotas());
        return paciente;
    }



}