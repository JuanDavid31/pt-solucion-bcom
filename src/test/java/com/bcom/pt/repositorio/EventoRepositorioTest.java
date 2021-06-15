package com.bcom.pt.repositorio;

import com.bcom.pt.entidad.Evento;
import com.bcom.pt.entidad.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EventoRepositorioTest extends DockerContainerPostgresTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private EventoRepositorio eventoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    private final LocalDateTime fecha = LocalDateTime.now().plus(1, DAYS);

    @Autowired
    private DataSource datasource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static boolean dataLoaded = false;

    @BeforeEach
    public void setup() {
        if (dataLoaded)return;
        try (Connection conn = datasource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/scripts/clean_db.sql"));
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
        Evento evento = em.find(Evento.class, 100);

        evento.setNombre("Nuevo nombre");
        eventoRepositorio.save(evento);
        evento = em.find(Evento.class, 100);

        assertEquals("Nuevo nombre", evento.getNombre());
    }

    @Test
    public void eliminarEvento() {
        eventoRepositorio.deleteById(100);

        Evento evento = em.find(Evento.class, 100);

        assertNull(evento);
    }

    @Test
    public void eliminarEventoYAsistencia() {
        eventoRepositorio.deleteById(200);

        Evento evento = em.find(Evento.class, 200);
        Usuario usuario = em.find(Usuario.class, 3);
        int result = ((Number) em.createNativeQuery("SELECT count(*) FROM asistencia")
            .getSingleResult()).intValue();

        assertEquals(0, result);
        assertNull(evento);
        assertThat(usuario.getEventosAsistidos()).isEmpty();
    }

    @Test
    public void agregarAsistenciaDesdeEvento() {
        Usuario usuario = usuarioRepositorio.getOne(1);
        Evento evento = eventoRepositorio.getOne(100);

        evento.agregarAsistente(usuario);
        eventoRepositorio.save(evento);

        Evento eventoEncontrado = em.find(Evento.class, 100);
        Usuario usuarioEncontrado = em.find(Usuario.class, 1);
        assertThat(eventoEncontrado.getAsistentes()).hasSize(1);
        assertThat(usuarioEncontrado.getEventosAsistidos()).hasSize(1);
    }

    @Test
    public void agregarAsistenciaDesdeUsuario() {
        Usuario usuario = usuarioRepositorio.getOne(1);
        Evento evento = eventoRepositorio.getOne(100);

        usuario.agregarEventoAsistido(evento);
        usuarioRepositorio.save(usuario);

        Evento eventoEncontrado = em.find(Evento.class, 100);
        Usuario usuarioEncontrado = em.find(Usuario.class, 1);
        assertThat(eventoEncontrado.getAsistentes()).hasSize(1);
        assertThat(usuarioEncontrado.getEventosAsistidos()).hasSize(1);
    }

}
