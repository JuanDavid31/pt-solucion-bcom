package com.bcom.pt.controlador;

import com.bcom.pt.entidad.Evento;
import com.bcom.pt.entidad.Usuario;
import com.bcom.pt.servicio.EventoServicio;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventoControlador.class)
public class EventoControladorTest {

    @MockBean
    private EventoServicio servicio;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void darEventosVacios() throws Exception {
        when(servicio.darEventos()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/eventos"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    public void darEventos() throws Exception {
        List<Evento> eventos = Collections.singletonList(new Evento());
        when(servicio.darEventos()).thenReturn(eventos);

        mockMvc.perform(get("/eventos"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void editarEventoInvalido() throws Exception {
        mockMvc.perform(put("/eventos/1")
                            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest());

        mockMvc.perform(put("/eventos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new Evento())))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void editarEventoValido() throws Exception {
        Evento evento = new Evento("Nombre", LocalDateTime.now());
        Evento eventoConId = new Evento("Nombre", LocalDateTime.now())
            .setId(10);

        when(servicio.editarEvento(eq(1), eq(evento))).thenReturn(eventoConId);

        mockMvc.perform(put("/eventos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(evento)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(10)))
            .andExpect(jsonPath("$.nombre", is("Nombre")))
            .andExpect(jsonPath("$.fecha", isA(String.class)));

        verify(servicio, times(1)).editarEvento(eq(1), eq(evento));
    }

    @Test
    public void eliminarEvento() throws Exception {
        mockMvc.perform(delete("/eventos/1")
                            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        verify(servicio, times(1)).eliminarEvento(eq(1));
    }
}
