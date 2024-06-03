package com.final_laboratorio4.final_laboratorio4.services.implementsService;

import com.final_laboratorio4.final_laboratorio4.DTO.LibroDTO;
import com.final_laboratorio4.final_laboratorio4.models.Libro;

import java.util.List;

public interface ImplLibro {
    LibroDTO crearLibro(LibroDTO libroDTO);
    boolean tienePrestamosActivos(Long libroId);
    List<Libro> buscarLibros(String query);
    LibroDTO obtenerLibroPorId(Long id);
    void eliminarLibro(Long id);
    LibroDTO modificarLibro(Long id, LibroDTO libroDTO);
    List<LibroDTO> obtenerTodosLosLibros();
}
