package com.final_laboratorio4.final_laboratorio4.repositories;

import com.final_laboratorio4.final_laboratorio4.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTitulo(String titulo);
}
