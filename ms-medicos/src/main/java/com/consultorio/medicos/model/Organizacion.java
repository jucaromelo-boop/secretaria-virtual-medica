package com.consultorio.medicos.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "organizaciones")
public class Organizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(unique = true)
    private String codigoIdentificador;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    protected Organizacion() {
    }

    public Organizacion(String nombre, String codigoIdentificador) {
        this.nombre = nombre;
        this.codigoIdentificador = codigoIdentificador;
        this.activo = true;
        this.fechaRegistro = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigoIdentificador() { return codigoIdentificador; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}