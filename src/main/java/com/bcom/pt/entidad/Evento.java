package com.bcom.pt.entidad;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

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

/**
 * {@code @Table y @Entity} Para nombrar la tabla en la DB y que JPA la reconozca <br>
 * {@code @Data} Para generar getters, setters, toString() y hashCode() <br>
 * {@code @Setter} Para eliminar los setters y forzar al dev a solo crear inmutables <br>
 * {@code @Builder} Para tener el builder method <br>
 * {@code @With} Para complementar con {@code @builder} <br>
 * Los {@code @...Constructor} Es para ayudar a JPA con la instanciación de objetos <br>
 */
@Table(name = "EVENTO")
@Entity
@Data
@Setter(value = AccessLevel.NONE)
@Builder
@With
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
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
    @Builder.Default
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
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_modificacion")
    @Builder.Default
    private LocalDateTime fechaModificacion = LocalDateTime.now();

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
        fechaModificacion = LocalDateTime.now();
    }

}