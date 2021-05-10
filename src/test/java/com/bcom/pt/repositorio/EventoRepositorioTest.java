package com.bcom.pt.repositorio;

import com.bcom.pt.entidad.Evento;
import com.bcom.pt.entidad.Usuario;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = { EventoRepositorioTest.Initializer.class })
//@Sql(scripts = "/scripts/second.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
public class EventoRepositorioTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private EventoRepositorio eventoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11");

    private final LocalDateTime fecha = LocalDateTime.now().plus(1, DAYS);

    @Autowired
    private DataSource datasource;

    private static boolean dataLoaded = false;

    @Before
    public void setup() {
        if (dataLoaded)return;
        try (Connection conn = datasource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/scripts/second.sql"));
            dataLoaded = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void darEventos() {
        Iterable<Evento> eventos = eventoRepositorio.findAll();
        assertThat(eventos).hasSize(2);
    }

    @Test
    public void agregarEventoConReferencia() {
        Usuario usuario = usuarioRepositorio.getOne(1);//Igual que em.getReference(...)
        Evento evento = eventoRepositorio.save(new Evento("Nuevo evento", fecha, usuario));

        Evento eventoAgregado = em.find(Evento.class, evento.getId());
        assertNotNull(eventoAgregado);
        assertEquals("Nuevo evento", eventoAgregado.getNombre());
        assertEquals(usuario, eventoAgregado.getCreador());
        assertEquals(fecha, eventoAgregado.getFecha());
        assertNotNull(eventoAgregado.getFechaCreacion());
        assertNotNull(eventoAgregado.getFechaModificacion());
    }

    /**
     * No es posible agregarle un evento a un usuario de esta manera.
     * Pero seria posible si se agrega un @OneToMany(cascade = CascasdeType.Persist)
     * a el dueño de la relación; osea la clase Usuario.
     */
    @Test
    public void agregarEventoDesdeReferenciaNoAgrega() {
        Usuario usuario = usuarioRepositorio.getOne(1);
        Evento evento = new Evento("Nuevo evento", fecha);
        usuario.agregarEvento(evento);
        usuarioRepositorio.save(usuario);

        assertThat(eventoRepositorio.findAll()).hasSize(2);
    }

    @Test
    public void editarEvento() {
        Evento evento = em.find(Evento.class, 1);

        evento.setNombre("Nuevo nombre");
        eventoRepositorio.save(evento);
        evento = em.find(Evento.class, 1);

        assertEquals("Nuevo nombre", evento.getNombre());
    }

    @Test
    public void eliminarEvento() {
        eventoRepositorio.deleteById(1);

        Evento evento = em.find(Evento.class, 1);

        assertNull(evento);
    }

    @Test
    public void agregarAsistenciaDesdeEvento() {
        Usuario usuario = usuarioRepositorio.getOne(1);
        Evento evento = eventoRepositorio.getOne(1);

        evento.agregarAsistente(usuario);
        eventoRepositorio.save(evento);

        Evento eventoEncontrado = em.find(Evento.class, 1);
        Usuario usuarioEncontrado = em.find(Usuario.class, 1);
        assertThat(eventoEncontrado.getAsistentes()).hasSize(1);
        assertThat(usuarioEncontrado.getEventosAsistidos()).hasSize(1);
    }

    @Test
    public void agregarAsistenciaDesdeUsuario() {
        Usuario usuario = usuarioRepositorio.getOne(1);
        Evento evento = eventoRepositorio.getOne(1);

        usuario.agregarEventoAsistido(evento);
        usuarioRepositorio.save(usuario);

        Evento eventoEncontrado = em.find(Evento.class, 1);
        Usuario usuarioEncontrado = em.find(Usuario.class, 1);
        assertThat(eventoEncontrado.getAsistentes()).hasSize(1);
        assertThat(usuarioEncontrado.getEventosAsistidos()).hasSize(1);
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
