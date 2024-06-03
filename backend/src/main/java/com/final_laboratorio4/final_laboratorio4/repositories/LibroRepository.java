package com.final_laboratorio4.final_laboratorio4.repositories;

import com.final_laboratorio4.final_laboratorio4.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTitulo(String titulo);

    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) LIKE LOWER(:query)||'%' " +
            "OR LOWER(l.autor) LIKE LOWER(:query)||'%' " +
            "OR LOWER(l.genero) LIKE LOWER(:query)||'%'")
    List<Libro> buscarPorQuery(@Param("query") String query);

}
