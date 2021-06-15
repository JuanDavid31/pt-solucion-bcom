package com.bcom.pt.controlador;

import com.bcom.pt.entidad.Evento;
import com.bcom.pt.servicio.EventoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuarios/{id}/eventos")
public class UsuarioEventoControlador extends ExceptionHandlerControlador {

    private final EventoServicio servicio;

    @Autowired
    public UsuarioEventoControlador(EventoServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<Evento> agregarEvento(@PathVariable int id, @Valid @RequestBody Evento evento) {
        Evento eventoNuevo = servicio.agregarEvento(id, evento);
        return ResponseEntity.ok(eventoNuevo);
    }
}
