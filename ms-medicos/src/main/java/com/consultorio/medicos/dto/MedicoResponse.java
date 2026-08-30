package com.consultorio.medicos.dto;

import com.consultorio.medicos.model.Medico;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class MedicoResponse {
    private Long id;
    private String nombreCompleto;
    private String cedulaProfesional;
    private String universidad;
    private Integer anioGraduacion;
    private boolean verificado;
    private EspecialidadResponse especialidadPrincipal;
    private Set<EspecialidadResponse> especialidadesSecundarias;
    private String biografia;
    private String fotoUrl;
    private Set<String> idiomas;
    private String telefonoPersonal;
    private String email;
    private Set<SeguroResponse> segurosAceptados;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaUltimaActualizacion;
    private boolean activo;
    private Long organizacionId;

    public MedicoResponse(Medico m) {
        this.id = m.getId();
        this.nombreCompleto = m.getNombreCompleto();
        this.cedulaProfesional = m.getCedulaProfesional();
        this.universidad = m.getUniversidad();
        this.anioGraduacion = m.getAnioGraduacion();
        this.verificado = m.isVerificado();
        this.especialidadPrincipal = new EspecialidadResponse(m.getEspecialidadPrincipal());
        this.especialidadesSecundarias = m.getEspecialidadesSecundarias().stream()
                .map(EspecialidadResponse::new).collect(Collectors.toSet());
        this.biografia = m.getBiografia();
        this.fotoUrl = m.getFotoUrl();
        this.idiomas = m.getIdiomas();
        this.telefonoPersonal = m.getTelefonoPersonal();
        this.email = m.getEmail();
        this.segurosAceptados = m.getSegurosAceptados().stream()
                .map(SeguroResponse::new).collect(Collectors.toSet());
        this.fechaRegistro = m.getFechaRegistro();
        this.fechaUltimaActualizacion = m.getFechaUltimaActualizacion();
        this.activo = m.isActivo();
        this.organizacionId = m.getOrganizacionId();
    }

    public Long getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getCedulaProfesional() { return cedulaProfesional; }
    public String getUniversidad() { return universidad; }
    public Integer getAnioGraduacion() { return anioGraduacion; }
    public boolean isVerificado() { return verificado; }
    public EspecialidadResponse getEspecialidadPrincipal() { return especialidadPrincipal; }
    public Set<EspecialidadResponse> getEspecialidadesSecundarias() { return especialidadesSecundarias; }
    public String getBiografia() { return biografia; }
    public String getFotoUrl() { return fotoUrl; }
    public Set<String> getIdiomas() { return idiomas; }
    public String getTelefonoPersonal() { return telefonoPersonal; }
    public String getEmail() { return email; }
    public Set<SeguroResponse> getSegurosAceptados() { return segurosAceptados; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public LocalDateTime getFechaUltimaActualizacion() { return fechaUltimaActualizacion; }
    public boolean isActivo() { return activo; }
    public Long getOrganizacionId() { return organizacionId; }
}