package com.final_laboratorio4.final_laboratorio4.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "id_libro")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Libro libro;

    @Column(name = "fecha_prestamo")
    private LocalDate fecha_prestamo;

    @Column(name = "fecha_devolucion")
    private LocalDate fecha_devolucion;

    @Column(name = "estado")
    private String estado;

}
