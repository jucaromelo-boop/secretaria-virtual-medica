package com.consultorio.medicos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "medicos")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Identificacion profesional ---
    @NotBlank
    @Column(nullable = false)
    private String nombreCompleto;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String cedulaProfesional;

    private String universidad;

    private Integer anioGraduacion;

    @Column(nullable = false)
    private boolean verificado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_principal_id", nullable = false)
    private Especialidad especialidadPrincipal;

    @ManyToMany
    @JoinTable(
            name = "medico_especialidades_secundarias",
            joinColumns = @JoinColumn(name = "medico_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidad_id")
    )
    private Set<Especialidad> especialidadesSecundarias = new HashSet<>();

    // --- Perfil orientado al paciente ---
    @Column(length = 2000)
    private String biografia;

    private String fotoUrl;

    @ElementCollection
    @CollectionTable(name = "medico_idiomas", joinColumns = @JoinColumn(name = "medico_id"))
    @Column(name = "idioma")
    private Set<String> idiomas = new HashSet<>();

    // --- Contacto directo ---
    private String telefonoPersonal;

    @Email
    private String email;

    // --- Seguros aceptados (a nivel medico; podria variar por consultorio en el futuro) ---
    @ManyToMany
    @JoinTable(
            name = "medico_seguros",
            joinColumns = @JoinColumn(name = "medico_id"),
            inverseJoinColumns = @JoinColumn(name = "seguro_id")
    )
    private Set<Seguro> segurosAceptados = new HashSet<>();

    // --- Metadatos ---
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    private LocalDateTime fechaUltimaActualizacion;

    @Column(nullable = false)
    private boolean activo = true;

    protected Medico() {
    }

    public Medico(String nombreCompleto, String cedulaProfesional, Especialidad especialidadPrincipal) {
        this.nombreCompleto = nombreCompleto;
        this.cedulaProfesional = cedulaProfesional;
        this.especialidadPrincipal = especialidadPrincipal;
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
        this.verificado = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaUltimaActualizacion = LocalDateTime.now();
    }

    // Getters y setters

    public Long getId() { return id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCedulaProfesional() { return cedulaProfesional; }
    public void setCedulaProfesional(String cedulaProfesional) { this.cedulaProfesional = cedulaProfesional; }

    public String getUniversidad() { return universidad; }
    public void setUniversidad(String universidad) { this.universidad = universidad; }

    public Integer getAnioGraduacion() { return anioGraduacion; }
    public void setAnioGraduacion(Integer anioGraduacion) { this.anioGraduacion = anioGraduacion; }

    public boolean isVerificado() { return verificado; }
    public void setVerificado(boolean verificado) { this.verificado = verificado; }

    public Especialidad getEspecialidadPrincipal() { return especialidadPrincipal; }
    public void setEspecialidadPrincipal(Especialidad especialidadPrincipal) { this.especialidadPrincipal = especialidadPrincipal; }

    public Set<Especialidad> getEspecialidadesSecundarias() { return especialidadesSecundarias; }
    public void setEspecialidadesSecundarias(Set<Especialidad> especialidadesSecundarias) { this.especialidadesSecundarias = especialidadesSecundarias; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public Set<String> getIdiomas() { return idiomas; }
    public void setIdiomas(Set<String> idiomas) { this.idiomas = idiomas; }

    public String getTelefonoPersonal() { return telefonoPersonal; }
    public void setTelefonoPersonal(String telefonoPersonal) { this.telefonoPersonal = telefonoPersonal; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Seguro> getSegurosAceptados() { return segurosAceptados; }
    public void setSegurosAceptados(Set<Seguro> segurosAceptados) { this.segurosAceptados = segurosAceptados; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public LocalDateTime getFechaUltimaActualizacion() { return fechaUltimaActualizacion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}