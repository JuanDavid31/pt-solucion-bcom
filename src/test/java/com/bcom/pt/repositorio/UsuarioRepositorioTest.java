package com.bcom.pt.repositorio;

import com.bcom.pt.entidad.Usuario;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;


@DataJpaTest// Recomendable leer la documentación
@RunWith(SpringRunner.class) //Junit 5 no necesita esta anotación
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = { UsuarioRepositorioTest.Initializer.class })
@Sql("/scripts/first.sql")
public class UsuarioRepositorioTest {

    @Autowired
    private UsuarioRepositorio repositorio;

    @Autowired
    private EntityManager em;

    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11");

    @Test
    public void darUsuariosInexistentes() {
        Iterable<Usuario> usuarios = repositorio.findAll();

        assertNotNull(usuarios);
        assertThat(usuarios).hasSize(3);
    }

    @Test
    public void darUsuariosTest() {
        List<Usuario> usuarioList = Arrays.asList(new Usuario("Dummy"),
                                                  new Usuario("Dummy2"),
                                                  new Usuario("Dummy3"));
        usuarioList.forEach(em::persist);

        Iterable<Usuario> usuarios = repositorio.findAll();
        assertThat(usuarios)
            .hasSize(6)
            .contains(usuarioList.get(0),
                      usuarioList.get(1),
                      usuarioList.get(2));
    }

    @Test
    public void agregarUsuario() {
        repositorio.save(new Usuario(""));

        Usuario usuario = em.find(Usuario.class, 4);

        assertNull(usuario);
    }

    @Test
    public void agregarUsuarioConFlush() {
        repositorio.saveAndFlush(new Usuario(""));

        Usuario usuario = em.find(Usuario.class, 4);

        assertNull(usuario);
    }

    @Test
    public void agregarMultiplesUsuarios() {
        repositorio.save(new Usuario(""));
        repositorio.save(new Usuario("Dummy"));

        Usuario usuario = em.find(Usuario.class, 4);
        Usuario usuario2 = em.find(Usuario.class, 5);

        assertNotNull(usuario);
        assertNotNull(usuario.getFechaModificacion());
        assertNotNull(usuario.getFechaCreacion());

        assertNotNull(usuario2);
        assertEquals("Dummy", usuario2.getNombre());
        assertNotNull(usuario.getFechaModificacion());
        assertNotNull(usuario.getFechaCreacion());
    }

    @Test
    public void consultarUsuario() {
        Optional<Usuario> usuarioOpt = repositorio.findById(1);

        assertThat(usuarioOpt).isEmpty();
    }

    @Test
    public void consultarUsuarioPorReferencia() {
        Usuario usuario = repositorio.getOne(1);

        assertNotNull(usuario);
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                "spring.jpa.properties.hibernate.format_sql=" + true,
                "spring.liquibase.enabled=" + true)
                .applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
