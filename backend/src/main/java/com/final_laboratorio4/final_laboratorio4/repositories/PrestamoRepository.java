package com.final_laboratorio4.final_laboratorio4.repositories;

import com.final_laboratorio4.final_laboratorio4.models.Libro;
import com.final_laboratorio4.final_laboratorio4.models.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByUsuarioId(Long idUsuario);
    List<Prestamo> findByLibroId(Long idLibro);
    List<Prestamo> findByLibroIdAndEstado(Long libroId, String estado);
    List<Prestamo> findByUsuarioIdAndEstado(Long usuarioId, String estado);

    @Query("SELECT p FROM Prestamo p JOIN p.usuario u JOIN p.libro l " +
            "WHERE LOWER(u.username) LIKE LOWER(CONCAT(:query, '%')) " +
            "OR LOWER(l.titulo) LIKE LOWER(CONCAT(:query, '%')) " +
            "OR LOWER(STR(p.fecha_prestamo)) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(STR(p.fecha_devolucion)) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.estado) LIKE LOWER(CONCAT(:query, '%'))")
    List<Prestamo> buscarPorQuery(String query);
}
