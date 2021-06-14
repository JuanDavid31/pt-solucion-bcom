package com.bcom.pt.controlador;

import com.bcom.pt.entidad.Evento;
import com.bcom.pt.entidad.Usuario;
import com.bcom.pt.servicio.EventoServicio;
import com.bcom.pt.servicio.UsuarioServicio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioEventoControlador.class)
public class UsuarioEventoControladorTest {

    @MockBean
    private EventoServicio servicio;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void agregarEventoInvalido() throws Exception {
        mockMvc.perform(post("/usuarios/1/eventos")
                            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest());

        mockMvc.perform(post("/usuarios/1/eventos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new Evento())))
            .andDo(print())
            .andExpect(status().isBadRequest());

        Evento evento = new Evento()
            .setNombre("abcde");

        mockMvc.perform(post("/usuarios/1/eventos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(evento)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void agregarEvento() throws Exception {
        Evento eventoValido = new Evento("Nombre", LocalDateTime.now());
        Evento eventoValidoConId = new Evento("Nombre", LocalDateTime.now())
            .setId(10);

        when(servicio.agregarEvento(eq(1), any())).thenReturn(eventoValidoConId);
        mockMvc.perform(post("/usuarios/1/eventos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(eventoValido)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(10)))
            .andExpect(jsonPath("$.nombre", is("Nombre")))
            .andExpect(jsonPath("$.fecha", isA(String.class)));
    }
}
