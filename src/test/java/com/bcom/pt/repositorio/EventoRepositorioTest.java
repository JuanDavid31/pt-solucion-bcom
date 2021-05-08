package com.bcom.pt.repositorio;

import com.bcom.pt.entidad.Evento;
import com.bcom.pt.entidad.Usuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DataJpaTest
@RunWith(SpringRunner.class)
public class EventoRepositorioTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private EventoRepositorio eventoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;



}
