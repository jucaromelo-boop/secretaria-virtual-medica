package com.consultorio.citas.service;

import com.consultorio.citas.exception.ListaEsperaNoEncontradaException;
import com.consultorio.citas.model.EstadoListaEspera;
import com.consultorio.citas.model.ListaEspera;
import com.consultorio.citas.repository.ListaEsperaRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ListaEsperaService {

    private final ListaEsperaRepository listaEsperaRepository;

    public ListaEsperaService(ListaEsperaRepository listaEsperaRepository) {
        this.listaEsperaRepository = listaEsperaRepository;
    }

    public ListaEspera registrar(Long pacienteId, Long medicoId, Set<DayOfWeek> diasPreferidos,
                                 LocalTime horaInicioPreferida, LocalTime horaFinPreferida, LocalDate fechaLimite) {
        ListaEspera entrada = new ListaEspera(pacienteId, medicoId, diasPreferidos,
                horaInicioPreferida, horaFinPreferida, fechaLimite);
        return listaEsperaRepository.save(entrada);
    }

    public ListaEspera buscarPorId(Long id) {
        return listaEsperaRepository.findById(id)
                .orElseThrow(() -> new ListaEsperaNoEncontradaException(id));
    }

    public List<ListaEspera> listarPorPaciente(Long pacienteId) {
        return listaEsperaRepository.findByPacienteIdAndEstado(pacienteId, EstadoListaEspera.ACTIVA);
    }

    public void cancelar(Long id) {
        ListaEspera entrada = buscarPorId(id);
        entrada.setEstado(EstadoListaEspera.CANCELADA);
        listaEsperaRepository.save(entrada);
    }

    public List<ListaEspera> buscarCandidatosParaHorario(Long medicoId, LocalDateTime fechaHoraLiberada) {
        DayOfWeek diaLiberado = fechaHoraLiberada.getDayOfWeek();
        LocalTime horaLiberada = fechaHoraLiberada.toLocalTime();
        LocalDate fechaLiberada = fechaHoraLiberada.toLocalDate();

        List<ListaEspera> candidatosDelMedico = listaEsperaRepository
                .findByMedicoIdAndEstado(medicoId, EstadoListaEspera.ACTIVA);

        return candidatosDelMedico.stream()
                .filter(c -> c.getDiasPreferidos().contains(diaLiberado))
                .filter(c -> !horaLiberada.isBefore(c.getHoraInicioPreferida())
                        && !horaLiberada.isAfter(c.getHoraFinPreferida()))
                .filter(c -> !fechaLiberada.isAfter(c.getFechaLimite()))
                .collect(Collectors.toList());
    }

    public void marcarOfrecida(Long id) {
        ListaEspera entrada = buscarPorId(id);
        entrada.setEstado(EstadoListaEspera.OFRECIDA);
        listaEsperaRepository.save(entrada);
    }

    public void marcarOcupada(Long id) {
        ListaEspera entrada = buscarPorId(id);
        entrada.setEstado(EstadoListaEspera.OCUPADA);
        listaEsperaRepository.save(entrada);
    }
}