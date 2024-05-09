package com.final_laboratorio4.final_laboratorio4.repositories;

import com.final_laboratorio4.final_laboratorio4.models.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByUsuarioId(Long idUsuario);
    List<Prestamo> findByLibroId(Long idLibro);
    List<Prestamo> findByLibroIdAndEstado(Long libroId, String estado);
    List<Prestamo> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}
