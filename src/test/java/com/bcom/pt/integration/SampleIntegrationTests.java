package com.bcom.pt.integration;


import com.bcom.pt.entidad.Usuario;
import com.bcom.pt.repositorio.UsuarioRepositorio;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SampleIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:11")
        .withUsername("duke")
        .withPassword("password")
        .withDatabaseName("test");

    // requires Spring Boot >= 2.2.6
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> true);
        registry.add("spring.liquibase.enabled", () -> true);
    }

    @Test
    @Order(1)
    void contextLoads() {
        System.out.println("Context loads!");
    }

    @Test
    @Order(2)
    public void darUsuariosVacios() throws Exception {
        mockMvc.perform(get("/usuarios"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    @Order(3)
    public void darUsuarios() throws Exception {
        usuarioRepositorio.save(new Usuario("jaja"));

        mockMvc.perform(get("/usuarios"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }
}
