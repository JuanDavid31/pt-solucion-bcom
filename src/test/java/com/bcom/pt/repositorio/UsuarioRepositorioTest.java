package com.bcom.pt.repositorio;

import com.bcom.pt.entidad.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UsuarioRepositorioTest extends DockerContainerPostgresTest {

    @Autowired
    private UsuarioRepositorio repositorio;

    @Autowired
    private TestEntityManager em;

    private static boolean dataLoaded = false;

    @BeforeEach
    public void setup() {
        if (dataLoaded)return;
        try (Connection conn = datasource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/scripts/clean_db.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/scripts/first.sql"));
            dataLoaded = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void darUsuarios() {
        Iterable<Usuario> usuarios = repositorio.findAll();

        assertNotNull(usuarios);
        assertThat(usuarios).hasSize(3);
    }

    @Test
    public void agregarUsuario() {
        Usuario usuarioGuardado = repositorio.save(new Usuario("Valido"));
        Usuario usuario = em.find(Usuario.class, usuarioGuardado.getId());

        assertNotNull(usuario);
    }

    @Test
    public void agregarMultiplesUsuarios() {
        repositorio.save(new Usuario("Dummy"));
        repositorio.save(new Usuario("Dummy2"));

        Usuario usuario = em.find(Usuario.class, 4);
        Usuario usuario2 = em.find(Usuario.class, 5);

        assertNotNull(usuario);
        assertNotNull(usuario.getFechaModificacion());
        assertNotNull(usuario.getFechaCreacion());

        assertNotNull(usuario2);
        assertEquals("Dummy2", usuario2.getNombre());
        assertNotNull(usuario.getFechaModificacion());
        assertNotNull(usuario.getFechaCreacion());
    }

    @Test
    public void consultarUsuario() {
        Optional<Usuario> usuarioOpt = repositorio.findById(1);

        assertThat(usuarioOpt).isPresent();
    }

    @Test
    public void consultarUsuarioPorReferencia() {
        Usuario usuario = repositorio.getOne(1);

        assertNotNull(usuario);
    }

    @Test
    public void consultarUsuarioInexistentePorReferencia() {
        Usuario usuario = repositorio.getOne(99);

        assertNotNull(usuario);
    }
}
