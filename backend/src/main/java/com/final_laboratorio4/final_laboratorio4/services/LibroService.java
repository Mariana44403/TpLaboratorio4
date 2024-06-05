package com.final_laboratorio4.final_laboratorio4.services;

import com.final_laboratorio4.final_laboratorio4.DTO.LibroDTO;
import com.final_laboratorio4.final_laboratorio4.models.Libro;
import com.final_laboratorio4.final_laboratorio4.models.Prestamo;
import com.final_laboratorio4.final_laboratorio4.repositories.LibroRepository;
import com.final_laboratorio4.final_laboratorio4.repositories.PrestamoRepository;
import com.final_laboratorio4.final_laboratorio4.services.implementsService.ImplLibro;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LibroService implements ImplLibro {

    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    private LibroDTO libroALibroDTO(Libro libro) {
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

    @Override
    public LibroDTO crearLibro(LibroDTO libroDTO) {
        Libro libro = new Libro();
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
        Libro libroGuardado = libroRepository.save(libro);
        return libroALibroDTO(libroGuardado);
    }

    @Override
    public boolean tienePrestamosActivos(Long libroId) {
        List<Prestamo> prestamosActivos = prestamoRepository.findByLibroIdAndEstado(libroId, "Prestado");
        return !prestamosActivos.isEmpty();
    }

    @Override
    public List<Libro> obtenerLibrosDisponibles() {
        return libroRepository.findByEstado("Disponible");
    }

    @Override
    public LibroDTO obtenerLibroPorId(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro con ID: " + id + " no encontrado"));
        return libroALibroDTO(libro);
    }

    @Override
    @Transactional
    public void eliminarLibro(Long id) {
        try {
            if (tienePrestamosActivos(id)) {
                throw new IllegalStateException("No se puede eliminar el libro porque tiene préstamos activos.");
            }

            List<Prestamo> prestamos = prestamoRepository.findByLibroId(id);
            for (Prestamo prestamo : prestamos) {
                prestamo.setLibroTitulo(prestamo.getLibro().getTitulo());
                prestamo.setLibro(null);
                prestamoRepository.save(prestamo);
            }

            Libro libro = libroRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));

            libroRepository.delete(libro);

        } catch (Exception e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
        }
    }

    @Override
    public LibroDTO modificarLibro(Long id, LibroDTO libroDTO) {
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

    public List<Libro> buscarLibros(String query) {
        return libroRepository.buscarPorQuery(query);
    }

    @Override
    public List<LibroDTO> obtenerTodosLosLibros() {
        List<Libro> libros = libroRepository.findAll();
        return libros.stream()
                .map(this::libroALibroDTO)
                .collect(Collectors.toList());
    }
}