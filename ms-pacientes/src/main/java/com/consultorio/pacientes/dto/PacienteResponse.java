package com.consultorio.pacientes.dto;

import com.consultorio.pacientes.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class PacienteResponse {

    private Long id;
    private String nombreCompleto;
    private String documentoIdentidad;
    private TipoDocumento tipoDocumento;
    private LocalDate fechaNacimiento;
    private Sexo sexo;
    private String telefono;
    private String telefonoAlternativo;
    private String email;
    private String direccion;
    private String ciudad;
    private String codigoPostal;
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaTelefono;
    private String contactoEmergenciaRelacion;
    private TipoSangre tipoSangre;
    private String alergias;
    private String condicionesCronicas;
    private String medicamentosActuales;
    private String seguroMedico;
    private String numeroPoliza;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaUltimaActualizacion;
    private boolean activo;
    private String notas;
    private Set<Long> organizacionIds;

    public PacienteResponse(Paciente p) {
        this.id = p.getId();
        this.nombreCompleto = p.getNombreCompleto();
        this.documentoIdentidad = p.getDocumentoIdentidad();
        this.tipoDocumento = p.getTipoDocumento();
        this.fechaNacimiento = p.getFechaNacimiento();
        this.sexo = p.getSexo();
        this.telefono = p.getTelefono();
        this.telefonoAlternativo = p.getTelefonoAlternativo();
        this.email = p.getEmail();
        this.direccion = p.getDireccion();
        this.ciudad = p.getCiudad();
        this.codigoPostal = p.getCodigoPostal();
        this.contactoEmergenciaNombre = p.getContactoEmergenciaNombre();
        this.contactoEmergenciaTelefono = p.getContactoEmergenciaTelefono();
        this.contactoEmergenciaRelacion = p.getContactoEmergenciaRelacion();
        this.tipoSangre = p.getTipoSangre();
        this.alergias = p.getAlergias();
        this.condicionesCronicas = p.getCondicionesCronicas();
        this.medicamentosActuales = p.getMedicamentosActuales();
        this.seguroMedico = p.getSeguroMedico();
        this.numeroPoliza = p.getNumeroPoliza();
        this.fechaRegistro = p.getFechaRegistro();
        this.fechaUltimaActualizacion = p.getFechaUltimaActualizacion();
        this.activo = p.isActivo();
        this.notas = p.getNotas();
        this.organizacionIds = p.getOrganizacionIds();
    }

    public Long getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public Sexo getSexo() { return sexo; }
    public String getTelefono() { return telefono; }
    public String getTelefonoAlternativo() { return telefonoAlternativo; }
    public String getEmail() { return email; }
    public String getDireccion() { return direccion; }
    public String getCiudad() { return ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
    public String getContactoEmergenciaNombre() { return contactoEmergenciaNombre; }
    public String getContactoEmergenciaTelefono() { return contactoEmergenciaTelefono; }
    public String getContactoEmergenciaRelacion() { return contactoEmergenciaRelacion; }
    public TipoSangre getTipoSangre() { return tipoSangre; }
    public String getAlergias() { return alergias; }
    public String getCondicionesCronicas() { return condicionesCronicas; }
    public String getMedicamentosActuales() { return medicamentosActuales; }
    public String getSeguroMedico() { return seguroMedico; }
    public String getNumeroPoliza() { return numeroPoliza; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public LocalDateTime getFechaUltimaActualizacion() { return fechaUltimaActualizacion; }
    public boolean isActivo() { return activo; }
    public String getNotas() { return notas; }
    public Set<Long> getOrganizacionIds() { return organizacionIds; }

}