package com.bcom.pt.controlador;

import com.bcom.pt.dto.Asistencia;
import com.bcom.pt.servicio.EventoServicio;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AsistenciaControlador.class)
public class AsistenciaControladorTest {

    @MockBean
    private EventoServicio servicio;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void agregarAsistenciaInvalido() throws Exception {
        mockMvc.perform(post("/asistencia")
                            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", isA(String.class)));

        Asistencia asistencia = new Asistencia();

        mockMvc.perform(post("/asistencia")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(asistencia)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.idUsuario", isA(String.class)))
            .andExpect(jsonPath("$.idEvento", isA(String.class)));

        when(servicio.agregarAsistencia(anyInt(), anyInt())).thenReturn(false);

        asistencia = new Asistencia(10, 20);

        mockMvc.perform(post("/asistencia")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(asistencia)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void agregarAsistencia() throws Exception {
        when(servicio.agregarAsistencia(anyInt(), anyInt())).thenReturn(true);

        Asistencia asistencia = new Asistencia(10, 20);

        mockMvc.perform(post("/asistencia")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(asistencia)))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
