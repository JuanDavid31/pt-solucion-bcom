package com.bcom.pt.entidad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "USUARIO")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String nombre;

    //Cuando algun evento setea su creador a null pueden pasar 2 cosas
    //Update a la tabla evento y poner null en la llave foranea (orphanRemoval = false)
    //Delete sobre la tabla evento (orphanRemoval = true)
    @OneToMany(mappedBy = "creador", orphanRemoval = true)
    private List<Evento> eventos = new ArrayList();

    @ManyToMany(mappedBy = "asistentes")
    private Set<Evento> eventosAsistidos = new HashSet<>();//Set es más optimo para @ManyToMany

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    public Usuario() {}

    public Usuario(String nombre) {
        this.nombre = nombre;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public Set<Evento> getEventosAsistidos() {
        return eventosAsistidos;
    }

    public void setEventosAsistidos(Set<Evento> eventosAsistidos) {
        this.eventosAsistidos = eventosAsistidos;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public void agregarEvento(Evento evento) {
        eventos.add(evento);
        evento.setCreador(this);
    }

    public void eliminarEvento(Evento evento) {
        eventos.remove(evento);
        evento.setCreador(null);
    }

    public void agregarEventoAsistido(Evento evento) {
        eventosAsistidos.add(evento);
        evento.getAsistentes().add(this);
    }

    public void eliminarEventoAsistido(Evento evento) {
        eventosAsistidos.remove(evento);
        evento.getAsistentes().remove(this);
    }

    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + id +
            ", nombre='" + nombre + '\'' +
            ", eventos=" + eventos.size() +
            ", fechaCreacion=" + fechaCreacion +
            ", fechaModificacion=" + fechaModificacion +
            '}';
    }
}