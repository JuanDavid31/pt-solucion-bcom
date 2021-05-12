package com.bcom.pt.servicio;

import com.bcom.pt.entidad.Evento;
import com.bcom.pt.entidad.Usuario;
import com.bcom.pt.repositorio.EventoRepositorio;
import com.bcom.pt.repositorio.UsuarioRepositorio;
import com.bcom.pt.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoServicio {

    private final EventoRepositorio eventoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public EventoServicio(EventoRepositorio eventoRepositorio,
                   UsuarioRepositorio usuarioRepositorio) {
        this.eventoRepositorio = eventoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public List<Evento> darEventos() {
        return eventoRepositorio.findAll();
    }

    public Evento editarEvento(int idEvento, Evento eventoNuevo) {
        Evento evento = eventoRepositorio.getOne(idEvento);
        MapperUtils.mapearEvento(evento, eventoNuevo);
        return eventoRepositorio.save(evento);
    }

    public Evento agregarEvento(int idUsuario, Evento evento) {
        Usuario usuario = usuarioRepositorio.getOne(idUsuario);
        evento.setCreador(usuario);
        return eventoRepositorio.save(evento);
    }

    public void eliminarEvento(int id) {
        eventoRepositorio.deleteById(id);
    }

    public boolean agregarAsistencia(int idUsuario, int idEvento) {
        Usuario usuario = usuarioRepositorio.getOne(idUsuario);
        Evento evento = eventoRepositorio.getOne(idEvento);
        evento.agregarAsistente(usuario);
        eventoRepositorio.save(evento);
        return true;
    }
}
