package com.consultorio.citas.service;

import com.consultorio.citas.config.CitasProperties;
import com.consultorio.citas.exception.CancelacionNoPermitidaException;
import com.consultorio.citas.exception.CitaNoEncontradaException;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private CitasProperties citasProperties;

    @InjectMocks
    private CitaService citaService;

    @Test
    void deberiaCrearCitaCuandoNoHayConflictoDeHorario() {
        // Arrange
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        when(citasProperties.getBufferEntreCitasMinutos()).thenReturn(10);
        when(citaRepository.findByMedicoNombreAndFechaHoraBetween(anyString(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cita resultado = citaService.crearCita("Juan Perez", "Dra. Gomez", fechaHora, 30);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getPacienteNombre()).isEqualTo("Juan Perez");
        assertThat(resultado.getMedicoNombre()).isEqualTo("Dra. Gomez");
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void deberiaUsarDuracionPorDefectoCuandoNoSeEspecifica() {
        // Arrange
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        when(citasProperties.getDuracionMinutosDefault()).thenReturn(30);
        when(citasProperties.getBufferEntreCitasMinutos()).thenReturn(10);
        when(citaRepository.findByMedicoNombreAndFechaHoraBetween(anyString(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act: no mandamos duracion (null)
        Cita resultado = citaService.crearCita("Juan Perez", "Dra. Gomez", fechaHora, null);

        // Assert
        assertThat(resultado.getDuracionMinutos()).isEqualTo(30);
    }

    @Test
    void noDeberiaLanzarConflictoSiLaCitaExistenteEstaCancelada() {
        // Arrange
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        Cita citaCancelada = new Cita("Otro Paciente", "Dra. Gomez", fechaHora, 30);
        citaCancelada.setEstado(EstadoCita.CANCELADA);

        when(citasProperties.getBufferEntreCitasMinutos()).thenReturn(10);
        when(citaRepository.findByMedicoNombreAndFechaHoraBetween(anyString(), any(), any()))
                .thenReturn(List.of(citaCancelada));
        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cita resultado = citaService.crearCita("Juan Perez", "Dra. Gomez", fechaHora, 30);

        // Assert
        assertThat(resultado).isNotNull();
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void noDeberiaPermitirCancelarConMenosDeLasHorasMinimas() {
        // Arrange
        Cita cita = new Cita("Juan Perez", "Dra. Gomez", LocalDateTime.now().plusMinutes(30), 30);
        when(citaRepository.findById(1L)).thenReturn(   Optional.of(cita));
        when(citasProperties.getHorasMinimasAnticipacionCancelacion()).thenReturn(2);

        // Act + Assert
        assertThatThrownBy(() -> citaService.cancelarCita(1L, "cambio de planes"))
                .isInstanceOf(CancelacionNoPermitidaException.class);

        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void deberiaPermitirCancelarConSuficienteAnticipacion() {
        // Arrange
        Cita cita = new Cita("Juan Perez", "Dra. Gomez", LocalDateTime.now().plusDays(1), 30);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citasProperties.getHorasMinimasAnticipacionCancelacion()).thenReturn(2);
        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cita resultado = citaService.cancelarCita(1L, "cambio de planes");

        // Assert
        assertThat(resultado.getEstado()).isEqualTo(EstadoCita.CANCELADA);
    }

    @Test
    void deberiaLanzarExcepcionCuandoLaCitaNoExiste() {
        // Arrange
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> citaService.buscarPorId(99L))
                .isInstanceOf(CitaNoEncontradaException.class)
                .hasMessageContaining("99");
    }


}