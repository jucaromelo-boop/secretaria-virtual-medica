package com.consultorio.citas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "citas")
public class CitasProperties {

    private int duracionMinutosDefault = 30;
    private int bufferEntreCitasMinutos = 10;
    private int horasMinimasAnticipacionCancelacion = 2;

    public int getDuracionMinutosDefault() {
        return duracionMinutosDefault;
    }

    public void setDuracionMinutosDefault(int duracionMinutosDefault) {
        this.duracionMinutosDefault = duracionMinutosDefault;
    }

    public int getBufferEntreCitasMinutos() {
        return bufferEntreCitasMinutos;
    }

    public void setBufferEntreCitasMinutos(int bufferEntreCitasMinutos) {
        this.bufferEntreCitasMinutos = bufferEntreCitasMinutos;
    }

    public int getHorasMinimasAnticipacionCancelacion() {
        return horasMinimasAnticipacionCancelacion;
    }

    public void setHorasMinimasAnticipacionCancelacion(int horasMinimasAnticipacionCancelacion) {
        this.horasMinimasAnticipacionCancelacion = horasMinimasAnticipacionCancelacion;
    }
}