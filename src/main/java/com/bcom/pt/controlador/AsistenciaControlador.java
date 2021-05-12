package com.bcom.pt.controlador;

import com.bcom.pt.servicio.EventoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asistencia")
public class AsistenciaControlador {

    private final EventoServicio servicio;

    @Autowired
    public AsistenciaControlador(EventoServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity agregarAsistencia(@RequestBody int idEvento, @RequestBody int idUsuario) {
        boolean seAgrego = servicio.agregarAsistencia(idUsuario, idEvento);
        if (seAgrego) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
