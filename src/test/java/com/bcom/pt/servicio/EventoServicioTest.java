package com.bcom.pt.servicio;

import com.bcom.pt.entidad.Evento;
import com.bcom.pt.repositorio.EventoRepositorio;
import com.bcom.pt.repositorio.UsuarioRepositorio;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventoServicioTest {

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @Mock
    private EventoRepositorio eventoRepositorio;

    @InjectMocks
    private EventoServicio servicio;

    @Test
    public void validInjections() {
        assertNotNull(usuarioRepositorio);
        assertNotNull(eventoRepositorio);
        assertNotNull(servicio);
    }

    @Test
    public void editarEvento() {
        Evento eventoViejo = new Evento()
            .setNombre("Viejo")
            .setDescripcion("Vieja")
            .setFecha(LocalDateTime.now().plusDays(1));

        Evento eventoNuevo = new Evento()
            .setNombre("Nuevo")
            .setDescripcion("Nueva")
            .setFecha(LocalDateTime.now().plusDays(2));

        when(eventoRepositorio.getOne(1)).thenReturn(eventoViejo);
        when(eventoRepositorio.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));// Se le indica a repositorio.save(...) que devuelva el primer argumento que recibe
        Evento eventoEditado = servicio.editarEvento(1, eventoNuevo);


        verify(eventoRepositorio, times(1)).getOne(1);
        verify(eventoRepositorio, times(1)).save(any());
        assertNotNull(eventoEditado);
        assertEquals(eventoEditado.getNombre(), eventoNuevo.getNombre());
        assertEquals(eventoEditado.getDescripcion(), eventoNuevo.getDescripcion());
        assertEquals(eventoEditado.getFecha(), eventoNuevo.getFecha());
    }
}
