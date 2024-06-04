package com.final_laboratorio4.final_laboratorio4.services;

import com.final_laboratorio4.final_laboratorio4.DTO.PrestamoDTO;
import com.final_laboratorio4.final_laboratorio4.repositories.UserRepository;
import com.final_laboratorio4.final_laboratorio4.models.Usuario;
import com.final_laboratorio4.final_laboratorio4.models.Libro;
import com.final_laboratorio4.final_laboratorio4.models.Prestamo;
import com.final_laboratorio4.final_laboratorio4.repositories.LibroRepository;
import com.final_laboratorio4.final_laboratorio4.repositories.PrestamoRepository;
import com.final_laboratorio4.final_laboratorio4.services.implementsService.ImplPrestamo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrestamoService implements ImplPrestamo {
    private final LibroRepository libroRepository;
    private final UserRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    @Transactional
    public Prestamo crearPrestamo(PrestamoDTO prestamoDTO) {
        Prestamo nuevoPrestamo = new Prestamo();

        Libro libro = libroRepository.findById(prestamoDTO.getId_libro())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el libro con el ID " + prestamoDTO.getId_libro()));

        Usuario usuario = usuarioRepository.findById(prestamoDTO.getId_usuario())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario con el ID " + prestamoDTO.getId_usuario()));

        nuevoPrestamo.setLibro(libro);
        nuevoPrestamo.setUsuario(usuario);
        nuevoPrestamo.setFecha_prestamo(prestamoDTO.getFecha_prestamo());

        nuevoPrestamo.setEstado("Prestado");

        Prestamo prestamoGuardado = prestamoRepository.save(nuevoPrestamo);

        libro.setEstado("Prestado");
        libroRepository.save(libro);

        return prestamoGuardado;
    }

    public List<PrestamoDTO> getAllPrestamos(){
        List<Prestamo> prestamos = prestamoRepository.findAll();
        return prestamos.stream()
                .map(this::convertirPrestamoDTO)
                .collect(Collectors.toList());
    }

    public PrestamoDTO getPrestamoById(Long idPrestamo){
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(()-> new IllegalArgumentException("No se encontro el prestamo con esta ID: "+idPrestamo));
        return convertirPrestamoDTO(prestamo);
    }

    @Transactional
    public void eliminarPrestamo(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prestamo no encontrado"));

        if ("Prestado".equals(prestamo.getEstado())) {
            Libro libro = prestamo.getLibro();
            if (libro != null) {
                libro.setEstado("Disponible");
                libroRepository.save(libro);
            }
        }

        prestamoRepository.delete(prestamo); // Elimina el préstamo
    }

    public boolean esPrestamoActivo(Long idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        return "Prestado".equals(prestamo.getEstado());
    }


    public boolean devolverPrestamo(Long idPrestamo, LocalDate fechaDevolucion) {
        Optional<Prestamo> prestamoOpt = prestamoRepository.findById(idPrestamo);
        if (prestamoOpt.isPresent()) {
            Prestamo prestamo = prestamoOpt.get();
            prestamo.setFecha_devolucion(fechaDevolucion);
            prestamo.setEstado("Devuelto");

            Libro libro = prestamo.getLibro();
            libro.setEstado("Disponible");
            libroRepository.save(libro);

            prestamoRepository.save(prestamo);

            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public ResponseEntity<PrestamoDTO> modificarPrestamo(Long id, PrestamoDTO prestamoDTO) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo con ID: " + id + " no encontrado"));

        if (prestamoDTO.getId_usuario() != null) {
            Usuario usuario = usuarioRepository.findById(prestamoDTO.getId_usuario())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            prestamo.setUsuario(usuario);
        }

        if (prestamoDTO.getId_libro() != null) {
            Libro libro = libroRepository.findById(prestamoDTO.getId_libro())
                    .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
            prestamo.setLibro(libro);
        }

        if (prestamoDTO.getFecha_prestamo() != null) {
            prestamo.setFecha_prestamo(prestamoDTO.getFecha_prestamo());
        }

        if (prestamoDTO.getFecha_devolucion() != null) {
            prestamo.setFecha_devolucion(prestamoDTO.getFecha_devolucion());
        }

        if (prestamoDTO.getEstado() != null) {
            prestamo.setEstado(prestamoDTO.getEstado());
        }

        Prestamo prestamoModificado = prestamoRepository.save(prestamo);

        PrestamoDTO resultado = convertirPrestamoDTO(prestamoModificado);

        return ResponseEntity.ok(resultado);
    }

    @Override
    public List<Prestamo> buscarPorQuery(String query) {
        return prestamoRepository.buscarPorQuery(query);
    }


    private PrestamoDTO convertirPrestamoDTO(Prestamo prestamo) {
        PrestamoDTO dto = new PrestamoDTO();
        dto.setId(prestamo.getId());
        dto.setId_usuario(prestamo.getUsuario().getId());
        dto.setId_libro(prestamo.getLibro().getId());
        dto.setFecha_prestamo(prestamo.getFecha_prestamo());
        dto.setFecha_devolucion(prestamo.getFecha_devolucion());
        dto.setEstado(prestamo.getEstado());
        return dto;
    }
}

