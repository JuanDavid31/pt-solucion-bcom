package com.bcom.pt.controlador;

import com.bcom.pt.dto.Asistencia;
import com.bcom.pt.servicio.EventoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/asistencia")
public class AsistenciaControlador extends ExceptionHandlerControlador {

    private final EventoServicio servicio;

    @Autowired
    public AsistenciaControlador(EventoServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity agregarAsistencia(@Valid  @RequestBody Asistencia asistencia) {
        boolean seAgrego = servicio.agregarAsistencia(asistencia.idUsuario, asistencia.idEvento);
        if (seAgrego) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
