package com.final_laboratorio4.final_laboratorio4.services.implementsService;

import com.final_laboratorio4.final_laboratorio4.DTO.PrestamoDTO;
import com.final_laboratorio4.final_laboratorio4.models.Prestamo;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface ImplPrestamo {
    Prestamo crearPrestamo(PrestamoDTO prestamoDTO);
    List<PrestamoDTO> getAllPrestamos();
    PrestamoDTO getPrestamoById(Long idPrestamo);
    void eliminarPrestamo(Long id);
    boolean esPrestamoActivo(Long idPrestamo);
    boolean devolverPrestamo(Long idPrestamo, LocalDate fechaDevolucion);
    ResponseEntity<PrestamoDTO> modificarPrestamo(Long id, PrestamoDTO prestamoDTO);
    List<Prestamo> buscarPorQuery(String query);
}
