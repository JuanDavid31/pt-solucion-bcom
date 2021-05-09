package com.bcom.pt.repositorio;

import com.bcom.pt.entidad.Evento;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = { EventoRepositorioTest.Initializer.class })
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

    @Test
    public void agregarEventoConReferencia() {
        em.persist(new Usuario("Dummy"));

        Usuario usuario = usuarioRepositorio.getOne(1);//Igual que em.getReference(...)
        Evento evento = new Evento("Nuevo evento", fecha, usuario);
        eventoRepositorio.save(evento);

        Evento eventoAgregado = em.find(Evento.class, 1);
        assertNotNull(eventoAgregado);
        assertEquals("Nuevo evento", eventoAgregado.getNombre());
        assertEquals(usuario, eventoAgregado.getCreador());
        assertEquals(fecha, eventoAgregado.getFecha());
        assertNotNull(eventoAgregado.getFechaCreacion());
        assertNotNull(eventoAgregado.getFechaModificacion());
    }


    @Test
    public void agregarEventoDesdeReferencia() {
        em.persist(new Usuario("Dummy"));

        Usuario usuario = usuarioRepositorio.getOne(1);
        Evento evento = new Evento("Nuevo evento", fecha);

        assertThatThrownBy(() -> usuario.agregarEvento(evento))
            .isInstanceOf(EntityNotFoundException.class);
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
