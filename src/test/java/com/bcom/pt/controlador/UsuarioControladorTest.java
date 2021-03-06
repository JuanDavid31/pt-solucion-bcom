package com.bcom.pt.controlador;

import com.bcom.pt.entidad.Usuario;
import com.bcom.pt.servicio.UsuarioServicio;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioControlador.class)
public class UsuarioControladorTest {

    @MockBean
    private UsuarioServicio servicio;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void darUsuariosVacios() throws Exception {
        when(servicio.darUsuarios()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/usuarios"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    public void darUsuarios() throws Exception {
        List<Usuario> usuarios = Collections.singletonList(new Usuario("jaja"));
        when(servicio.darUsuarios()).thenReturn(usuarios);

        mockMvc.perform(get("/usuarios"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void agregarUsuarioInvalido() throws Exception {
        mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", isA(String.class)));

        mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new Usuario())))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.nombre", isA(String.class)));

        mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new Usuario("abc"))))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.nombre", isA(String.class)));
    }

    @Test
    public void agregarUsuarioValido() throws Exception {
        Usuario usuarioValidoConId = new Usuario("valido")
            .setId(11);

        when(servicio.agregarUsuario(any())).thenReturn(usuarioValidoConId);
        mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new Usuario("valido"))))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(11)))
            .andExpect(jsonPath("$.nombre", is("valido")));
    }

}
