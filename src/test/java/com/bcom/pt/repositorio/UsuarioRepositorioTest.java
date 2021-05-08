package com.bcom.pt.repositorio;

import com.bcom.pt.entidad.Usuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@DataJpaTest// Recomendable leer la documentación
@RunWith(SpringRunner.class) //Junit 5 no necesita esta anotación
public class UsuarioRepositorioTest {

    @Autowired
    private UsuarioRepositorio repositorio;

    @Autowired
    private EntityManager em;

    @Test
    public void darUsuariosInexistentes() {
        Iterable<Usuario> usuarios = repositorio.findAll();
        assertNotNull(usuarios);
        assertThat(usuarios).isEmpty();
    }

    @Test
    public void darUsuariosTest() {
        List<Usuario> usuarioList = Arrays.asList(new Usuario("Dummy"),
                                                  new Usuario("Dummy2"),
                                                  new Usuario("Dummy3"));
        usuarioList.forEach(em::persist);

        Iterable<Usuario> usuarios = repositorio.findAll();
        assertThat(usuarios)
            .hasSize(3)
            .contains(usuarioList.get(0),
                      usuarioList.get(1),
                      usuarioList.get(2));
    }

    @Test
    public void agregarUsuario() {
        repositorio.save(new Usuario(""));
        repositorio.save(new Usuario("Dummy"));

        Usuario usuario = em.find(Usuario.class, 1);
        Usuario usuario2 = em.find(Usuario.class, 2);

        assertNotNull(usuario);
        assertNotNull(usuario.getFechaModificacion());
        assertNotNull(usuario.getFechaCreacion());

        assertNotNull(usuario2);
        assertEquals("Dummy", usuario2.getNombre());
        assertNotNull(usuario.getFechaModificacion());
        assertNotNull(usuario.getFechaCreacion());
    }
}
