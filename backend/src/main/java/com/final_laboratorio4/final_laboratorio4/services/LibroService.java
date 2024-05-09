package com.final_laboratorio4.final_laboratorio4.services;

import com.final_laboratorio4.final_laboratorio4.DTO.LibroDTO;
import com.final_laboratorio4.final_laboratorio4.models.Libro;
import com.final_laboratorio4.final_laboratorio4.models.Prestamo;
import com.final_laboratorio4.final_laboratorio4.repositories.LibroRepository;
import com.final_laboratorio4.final_laboratorio4.repositories.PrestamoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private PrestamoRepository prestamoRepository;

    public LibroDTO libroALibroDTO(Libro libro) {
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setId(libro.getId());
        libroDTO.setTitulo(libro.getTitulo());
        libroDTO.setAutor(libro.getAutor());
        libroDTO.setGenero(libro.getGenero());
        libroDTO.setNum_pagina(libro.getNum_pagina());
        libroDTO.setSinopsis(libro.getSinopsis());
        libroDTO.setEstado(libro.getEstado());
        return libroDTO;
    }
    public LibroDTO crearLibro(LibroDTO libroDTO) {
        Libro libro = new Libro();
        libro.setTitulo(libroDTO.getTitulo());
        libro.setAutor(libroDTO.getAutor());
        libro.setGenero(libroDTO.getGenero());
        libro.setNum_pagina(libroDTO.getNum_pagina());
        libro.setSinopsis(libroDTO.getSinopsis());
        // Si el estado no está definido, usar "disponible"
        if (libroDTO.getEstado() == null) {
            libro.setEstado("Disponible");
        } else {
            libro.setEstado(libroDTO.getEstado());
        }

        Libro libroGuardado = libroRepository.save(libro);
        return libroALibroDTO(libroGuardado);
    }

    // Verifica si un libro tiene préstamos activos
    public boolean tienePrestamosActivos(Long libroId) {
        List<Prestamo> prestamosActivos = prestamoRepository.findByLibroIdAndEstado(libroId, "Prestado");
        return !prestamosActivos.isEmpty();
    }

    public LibroDTO obtenerLibroPorId(Long id) {
        Libro libro =  libroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro con ID: " + id + " no encontrado"));

        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setId(libro.getId());
        libroDTO.setTitulo(libro.getTitulo());
        libroDTO.setAutor(libro.getAutor());
        libroDTO.setGenero(libro.getGenero());
        libroDTO.setNum_pagina(libro.getNum_pagina());
        libroDTO.setSinopsis(libro.getSinopsis());
        libroDTO.setEstado(libro.getEstado());

        return libroDTO;
    }


    @Transactional
    public void eliminarLibro(Long id) {
        try {
            if (tienePrestamosActivos(id)) {
                throw new IllegalStateException("No se puede eliminar el libro porque tiene préstamos activos.");
            }

            List<Prestamo> prestamos = prestamoRepository.findByLibroId(id);
            for (Prestamo prestamo : prestamos) {
                prestamo.setLibro(null); // Desasignar el libro
                prestamoRepository.save(prestamo); // Guardar cambios
            }

            Libro libro = libroRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));

            libroRepository.delete(libro); // Eliminar el libro

        } catch (Exception e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            // Aquí puedes decidir cómo manejar el error, por ejemplo, lanzar una excepción personalizada o devolver una respuesta específica al frontend.
        }
    }

    public LibroDTO modificarLibro (Long id, LibroDTO libroDTO) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro con ID: " + id + " no encontrado "));

        libro.setTitulo(libroDTO.getTitulo());
        libro.setAutor(libroDTO.getAutor());
        libro.setGenero(libroDTO.getGenero());
        libro.setNum_pagina(libroDTO.getNum_pagina());
        libro.setSinopsis(libroDTO.getSinopsis());
        if (libroDTO.getEstado() == null) {
            libro.setEstado("Disponible");
        } else {
            libro.setEstado(libroDTO.getEstado());
        }

        Libro libroModificado = libroRepository.save(libro);

        return libroALibroDTO(libroModificado);
    }

    public List<LibroDTO> obtenerTodosLosLibros() {
        List<Libro> libros = libroRepository.findAll(); // Obtiene todos los registros del repositorio
        // Convertir a DTO
        return libros.stream()
                .map(this::libroALibroDTO) // Convierte cada libro a su correspondiente DTO
                .collect(Collectors.toList()); // Recolecta en una lista
    }
}