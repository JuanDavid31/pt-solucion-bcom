package com.bcom.pt.repositorio;

import com.bcom.pt.entidad.Usuario;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;


@DataJpaTest// Recomendable leer la documentación
@RunWith(SpringRunner.class) //Junit 5 no necesita esta anotación
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = { UsuarioRepositorioTest.Initializer.class })
//@Sql(scripts = "/scripts/first.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)) //Se ejecuta una vez por @Test
public class UsuarioRepositorioTest {

    @Autowired
    private UsuarioRepositorio repositorio;

    @Autowired
    private TestEntityManager em;

    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11");

    @Autowired
    private DataSource datasource;

    private static boolean dataLoaded = false;

    @Before
    public void setup() {
        if (dataLoaded)return;
        try (Connection conn = datasource.getConnection()) {
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
