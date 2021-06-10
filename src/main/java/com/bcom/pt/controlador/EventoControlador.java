package com.bcom.pt.controlador;

import com.bcom.pt.entidad.Evento;
import com.bcom.pt.servicio.EventoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoControlador {

    private final EventoServicio servicio;

    @Autowired
    public EventoControlador(EventoServicio eventoServicio) {
        this.servicio = eventoServicio;
    }

    @GetMapping
    public ResponseEntity<List<Evento>> darEventos() {
        return ResponseEntity.ok(servicio.darEventos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> editarEvento(@PathVariable int id, @Valid @RequestBody Evento evento) {
        Evento eventoActualizado = servicio.editarEvento(id, evento);
        return ResponseEntity.ok(eventoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarEvento(@PathVariable int id) {
        System.out.println("Jelou evento " + id);
        servicio.eliminarEvento(id);
        return ResponseEntity.ok().build();
    }

}
