package com.consultorio.citas.service;

import com.consultorio.citas.client.MedicoClient;
import com.consultorio.citas.client.PacienteClient;
import com.consultorio.citas.config.CitasProperties;
import com.consultorio.citas.dto.MedicoDTO;
import com.consultorio.citas.dto.PacienteDTO;
import com.consultorio.citas.exception.CancelacionNoPermitidaException;
import com.consultorio.citas.exception.CitaNoEncontradaException;
import com.consultorio.citas.exception.ConflictoHorarioException;
import com.consultorio.citas.model.Cita;
import com.consultorio.citas.model.EstadoCita;
import com.consultorio.citas.repository.CitaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.consultorio.citas.event.CitaEventPublisher;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private CitasProperties citasProperties;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private MedicoClient medicoClient;

    @Mock
    private CitaEventPublisher citaEventPublisher;

    @InjectMocks
    private CitaService citaService;

    private PacienteDTO pacienteActivo(Long id) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(id);
        dto.setNombreCompleto("Juan Perez");
        dto.setActivo(true);
        return dto;
    }

    private MedicoDTO medicoActivo(Long id) {
        MedicoDTO dto = new MedicoDTO();
        dto.setId(id);
        dto.setNombreCompleto("Dra. Gomez");
        dto.setActivo(true);
        return dto;
    }

    @Test
    void deberiaCrearCitaCuandoNoHayConflictoDeHorario() {
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        when(pacienteClient.obtenerPaciente(1L)).thenReturn(pacienteActivo(1L));
        when(medicoClient.obtenerMedico(1L)).thenReturn(medicoActivo(1L));
        when(citasProperties.getBufferEntreCitasMinutos()).thenReturn(10);
        when(citaRepository.findByMedicoIdAndFechaHoraBetween(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cita resultado = citaService.crearCita(1L, 1L, fechaHora, 30);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getPacienteId()).isEqualTo(1L);
        assertThat(resultado.getMedicoId()).isEqualTo(1L);
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void deberiaUsarDuracionPorDefectoCuandoNoSeEspecifica() {
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        when(pacienteClient.obtenerPaciente(1L)).thenReturn(pacienteActivo(1L));
        when(medicoClient.obtenerMedico(1L)).thenReturn(medicoActivo(1L));
        when(citasProperties.getDuracionMinutosDefault()).thenReturn(30);
        when(citasProperties.getBufferEntreCitasMinutos()).thenReturn(10);
        when(citaRepository.findByMedicoIdAndFechaHoraBetween(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cita resultado = citaService.crearCita(1L, 1L, fechaHora, null);

        assertThat(resultado.getDuracionMinutos()).isEqualTo(30);
    }

    @Test
    void deberiaLanzarConflictoHorarioCuandoMedicoYaTieneCitaEnEseRango() {
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        Cita citaExistente = new Cita(99L, 1L, fechaHora, 30);

        when(pacienteClient.obtenerPaciente(1L)).thenReturn(pacienteActivo(1L));
        when(medicoClient.obtenerMedico(1L)).thenReturn(medicoActivo(1L));
        when(citasProperties.getBufferEntreCitasMinutos()).thenReturn(10);
        when(citaRepository.findByMedicoIdAndFechaHoraBetween(any(), any(), any()))
                .thenReturn(List.of(citaExistente));

        assertThatThrownBy(() ->
                citaService.crearCita(1L, 1L, fechaHora, 30))
                .isInstanceOf(ConflictoHorarioException.class);

        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void noDeberiaLanzarConflictoSiLaCitaExistenteEstaCancelada() {
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        Cita citaCancelada = new Cita(99L, 1L, fechaHora, 30);
        citaCancelada.setEstado(EstadoCita.CANCELADA);

        when(pacienteClient.obtenerPaciente(1L)).thenReturn(pacienteActivo(1L));
        when(medicoClient.obtenerMedico(1L)).thenReturn(medicoActivo(1L));
        when(citasProperties.getBufferEntreCitasMinutos()).thenReturn(10);
        when(citaRepository.findByMedicoIdAndFechaHoraBetween(any(), any(), any()))
                .thenReturn(List.of(citaCancelada));
        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cita resultado = citaService.crearCita(1L, 1L, fechaHora, 30);

        assertThat(resultado).isNotNull();
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void noDeberiaPermitirCancelarConMenosDeLasHorasMinimas() {
        Cita cita = new Cita(1L, 1L, LocalDateTime.now().plusMinutes(30), 30);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citasProperties.getHorasMinimasAnticipacionCancelacion()).thenReturn(2);

        assertThatThrownBy(() -> citaService.cancelarCita(1L, "cambio de planes"))
                .isInstanceOf(CancelacionNoPermitidaException.class);

        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void deberiaPermitirCancelarConSuficienteAnticipacion() {
        Cita cita = new Cita(1L, 1L, LocalDateTime.now().plusDays(1), 30);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citasProperties.getHorasMinimasAnticipacionCancelacion()).thenReturn(2);
        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cita resultado = citaService.cancelarCita(1L, "cambio de planes");

        assertThat(resultado.getEstado()).isEqualTo(EstadoCita.CANCELADA);
    }

    @Test
    void deberiaLanzarExcepcionCuandoLaCitaNoExiste() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> citaService.buscarPorId(99L))
                .isInstanceOf(CitaNoEncontradaException.class)
                .hasMessageContaining("99");
    }
}