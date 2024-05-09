package com.final_laboratorio4.final_laboratorio4.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "libro")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "autor")
    private String autor;
    @Column(name = "genero")
    private String genero;
    @Column(name = "num_pagina")
    private int num_pagina;
    @Column(name = "sinopsis")
    private String sinopsis;
    @Column(name = "estado")
    private String estado = "Disponible";
}
