package com.consultorio.pacientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Identificacion ---
    @NotBlank
    @Column(nullable = false)
    private String nombreCompleto;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String documentoIdentidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipoDocumento;

    @Past
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    // --- Contacto ---
    private String telefono;
    private String telefonoAlternativo;

    @Email
    private String email;

    private String direccion;
    private String ciudad;
    private String codigoPostal;

    // --- Contacto de emergencia ---
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaTelefono;
    private String contactoEmergenciaRelacion;

    // --- Informacion clinica basica ---
    @Enumerated(EnumType.STRING)
    private TipoSangre tipoSangre;

    @Column(length = 1000)
    private String alergias;

    @Column(length = 1000)
    private String condicionesCronicas;

    @Column(length = 1000)
    private String medicamentosActuales;

    // --- Seguro ---
    private String seguroMedico;
    private String numeroPoliza;

    // --- Metadatos ---
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    private LocalDateTime fechaUltimaActualizacion;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(length = 2000)
    private String notas;

    private String parentesco;

    protected Paciente() {
        // constructor vacio requerido por JPA
    }

    public Paciente(String nombreCompleto, String documentoIdentidad, TipoDocumento tipoDocumento) {
        this.nombreCompleto = nombreCompleto;
        this.documentoIdentidad = documentoIdentidad;
        this.tipoDocumento = tipoDocumento;
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaUltimaActualizacion = LocalDateTime.now();
    }

    // Getters y setters

    public Long getId() { return id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTelefonoAlternativo() { return telefonoAlternativo; }
    public void setTelefonoAlternativo(String telefonoAlternativo) { this.telefonoAlternativo = telefonoAlternativo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getContactoEmergenciaNombre() { return contactoEmergenciaNombre; }
    public void setContactoEmergenciaNombre(String contactoEmergenciaNombre) { this.contactoEmergenciaNombre = contactoEmergenciaNombre; }

    public String getContactoEmergenciaTelefono() { return contactoEmergenciaTelefono; }
    public void setContactoEmergenciaTelefono(String contactoEmergenciaTelefono) { this.contactoEmergenciaTelefono = contactoEmergenciaTelefono; }

    public String getContactoEmergenciaRelacion() { return contactoEmergenciaRelacion; }
    public void setContactoEmergenciaRelacion(String contactoEmergenciaRelacion) { this.contactoEmergenciaRelacion = contactoEmergenciaRelacion; }

    public TipoSangre getTipoSangre() { return tipoSangre; }
    public void setTipoSangre(TipoSangre tipoSangre) { this.tipoSangre = tipoSangre; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getCondicionesCronicas() { return condicionesCronicas; }
    public void setCondicionesCronicas(String condicionesCronicas) { this.condicionesCronicas = condicionesCronicas; }

    public String getMedicamentosActuales() { return medicamentosActuales; }
    public void setMedicamentosActuales(String medicamentosActuales) { this.medicamentosActuales = medicamentosActuales; }

    public String getSeguroMedico() { return seguroMedico; }
    public void setSeguroMedico(String seguroMedico) { this.seguroMedico = seguroMedico; }

    public String getNumeroPoliza() { return numeroPoliza; }
    public void setNumeroPoliza(String numeroPoliza) { this.numeroPoliza = numeroPoliza; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public LocalDateTime getFechaUltimaActualizacion() { return fechaUltimaActualizacion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getParentesco() { return parentesco; }
    public void setParentesco(String parentesco) { this.parentesco = parentesco; }
}