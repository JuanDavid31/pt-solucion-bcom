package com.bcom.pt.entidad;

import liquibase.pro.packaged.C;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "USUARIO")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String nombre;

    @OneToMany(mappedBy = "creador")
    private List<Evento> eventos = new ArrayList();

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