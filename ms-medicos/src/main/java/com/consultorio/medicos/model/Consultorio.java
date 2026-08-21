package com.consultorio.medicos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name = "consultorios")
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @NotBlank
    @Column(nullable = false)
    private String nombreConsultorio;

    @NotBlank
    @Column(nullable = false)
    private String direccion;

    private String ciudad;

    private String codigoPostal;

    private String telefonoConsultorio;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaConsulta;

    @Column(nullable = false)
    private Integer duracionConsultaMinutos = 30;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(unique = true)
    private String numeroWhatsapp;
    @Column(nullable = false)
    private Long organizacionId;

    protected Consultorio() {
    }

    public Consultorio(Medico medico, String nombreConsultorio, String direccion, BigDecimal tarifaConsulta) {
        this.medico = medico;
        this.nombreConsultorio = nombreConsultorio;
        this.direccion = direccion;
        this.tarifaConsulta = tarifaConsulta;
        this.organizacionId = medico.getOrganizacionId();
        this.activo = true;
    }
    public Long getId() { return id; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public String getNombreConsultorio() { return nombreConsultorio; }
    public void setNombreConsultorio(String nombreConsultorio) { this.nombreConsultorio = nombreConsultorio; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getTelefonoConsultorio() { return telefonoConsultorio; }
    public void setTelefonoConsultorio(String telefonoConsultorio) { this.telefonoConsultorio = telefonoConsultorio; }

    public BigDecimal getTarifaConsulta() { return tarifaConsulta; }
    public void setTarifaConsulta(BigDecimal tarifaConsulta) { this.tarifaConsulta = tarifaConsulta; }

    public Integer getDuracionConsultaMinutos() { return duracionConsultaMinutos; }
    public void setDuracionConsultaMinutos(Integer duracionConsultaMinutos) { this.duracionConsultaMinutos = duracionConsultaMinutos; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getNumeroWhatsapp() { return numeroWhatsapp; }
    public void setNumeroWhatsapp(String numeroWhatsapp) { this.numeroWhatsapp = numeroWhatsapp; }

    public Long getOrganizacionId() { return organizacionId; }
    public void setOrganizacionId(Long organizacionId) { this.organizacionId = organizacionId; }
}