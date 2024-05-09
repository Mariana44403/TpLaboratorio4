package com.final_laboratorio4.final_laboratorio4.DTO;

public class LibroDTO {
    private Long id;
    private String titulo;
    private String autor;
    private String genero;
    private int num_pagina;
    private String sinopsis;
    private String estado;

    public LibroDTO() {}

    public LibroDTO(Long id, String titulo, String autor, String genero, int num_pagina, String sinopsis, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.num_pagina = num_pagina;
        this.sinopsis = sinopsis;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getNum_pagina() {
        return num_pagina;
    }

    public void setNum_pagina(int num_pagina) {
        this.num_pagina = num_pagina;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }
}

