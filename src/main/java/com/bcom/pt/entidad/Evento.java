package com.bcom.pt.entidad;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "EVENTO")
@Entity
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_creador")
    private Usuario creador;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "ASISTENCIA",
        joinColumns = @JoinColumn(name = "id_evento", nullable = false),
        inverseJoinColumns = @JoinColumn(name="id_usuario", nullable = false)
    )
    private Set<Usuario> asistentes = new HashSet<>(); //Set es más optimo para @ManyToMany

    @Column
    @NotNull(message = "nombre no puede ser null")
    @Size(min = 4, max = 30, message = "nombre debe tener entre 4 y 30 caracteres")
    private String nombre;

    @Column
    private String descripcion;

    @Column
    @NotNull(message = "fecha no puede ser null")
    private LocalDateTime fecha;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion = LocalDateTime.now();

    public Evento() {
    }

    public Evento(String nombre, LocalDateTime fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public Evento(String nombre, LocalDateTime fecha, Usuario creador) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.creador = creador;
    }

    public int getId() {
        return id;
    }

    public Evento setId(int id) {
        this.id = id;
        return this;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public Set<Usuario> getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(Set<Usuario> asistentes) {
        this.asistentes = asistentes;
    }

    public String getNombre() {
        return nombre;
    }

    public Evento setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Evento setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Evento setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
        return this;
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

    public void agregarAsistente(Usuario usuario) {
        asistentes.add(usuario);
        usuario.getEventosAsistidos().add(this);
    }

    public void eliminarAsistente(Usuario usuario) {
        asistentes.remove(usuario);
        usuario.getEventosAsistidos().remove(this);
    }

    @PreUpdate
    public void actualizarFechaModificacion() {
        setFechaModificacion(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) { //TODO: El equals debe comparar todos los atributos
        if (this == o) return true;
        if (!(o instanceof Evento )) return false;
        return id == (((Evento) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Evento{" +
            "id=" + id +
            ", creador=" + (creador != null) +
            ", asistentes=" + asistentes.size() +
            ", nombre='" + nombre + '\'' +
            ", descripcion='" + descripcion + '\'' +
            ", fecha=" + fecha +
            ", fechaCreacion=" + fechaCreacion +
            ", fechaModificacion=" + fechaModificacion +
            '}';
    }
}