package com.final_laboratorio4.final_laboratorio4.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class LibroDTO {
    private Long id;
    private String titulo;
    private String autor;
    private String genero;
    private int num_pagina;
    private String sinopsis;
    private String estado;
}

