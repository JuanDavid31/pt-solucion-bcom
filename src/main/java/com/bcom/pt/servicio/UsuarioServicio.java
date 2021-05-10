package com.bcom.pt.servicio;

import com.bcom.pt.entidad.Usuario;
import com.bcom.pt.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio repositorio;

    public UsuarioServicio(@Autowired UsuarioRepositorio usuarioRepositorio) {
        this.repositorio = usuarioRepositorio;
    }

    public List<Usuario> darUsuarios() {
        return repositorio.findAll();
    }

    public Usuario agregarUsuario(Usuario usuario) {
        return repositorio.save(usuario);
    }

}
