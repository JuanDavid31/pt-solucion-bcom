package com.bcom.pt.servicio;

import com.bcom.pt.entidad.Usuario;
import com.bcom.pt.repositorio.UsuarioRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServicioTest {

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @InjectMocks
    private UsuarioServicio servicio;

    @Test
    public void validInjections() {
        assertNotNull(usuarioRepositorio);
        assertNotNull(servicio);
    }

    @Test
    public void darUsuarios(){
        ArrayList<Usuario> listaVacia = new ArrayList<>();

        when(usuarioRepositorio.findAll()).thenReturn(listaVacia);
        List<Usuario> usuarios = servicio.darUsuarios();

        assertEquals(listaVacia, usuarios);
        verify(usuarioRepositorio, times(1)).findAll();
    }

    @Test
    public void agregarUsuario() {
        Usuario usuarioVacio = new Usuario();
        Usuario usuarioConNombre = new Usuario("Nombre");

        when(usuarioRepositorio.save(eq(usuarioVacio))).thenReturn(usuarioConNombre);
        Usuario usuarioAgregado = servicio.agregarUsuario(usuarioVacio);

        assertEquals(usuarioConNombre, usuarioAgregado);
        verify(usuarioRepositorio, times(1)).save(eq(usuarioVacio));
    }

}
