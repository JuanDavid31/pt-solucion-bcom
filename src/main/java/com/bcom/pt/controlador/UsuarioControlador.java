package com.bcom.pt.controlador;

import com.bcom.pt.entidad.Usuario;
import com.bcom.pt.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador extends ExceptionHandlerControlador {

    private final UsuarioServicio servicio;

    @Autowired
    public UsuarioControlador(UsuarioServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> darUsuarios() {
        return ResponseEntity.ok(servicio.darUsuarios());
    }

    @PostMapping
    public ResponseEntity<Usuario> agregarUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario usuarioAgregado = servicio.agregarUsuario(usuario);
        return ResponseEntity.ok(usuarioAgregado);
    }


}
