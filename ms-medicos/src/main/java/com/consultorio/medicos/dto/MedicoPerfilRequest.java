package com.consultorio.medicos.dto;

import jakarta.validation.constraints.Email;

import java.util.Set;

public class MedicoPerfilRequest {
    private String biografia;
    private String fotoUrl;
    private Set<String> idiomas;
    private String telefonoPersonal;

    @Email(message = "El email no tiene un formato valido")
    private String email;

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
}