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

    public Prestamo crearPrestamo(PrestamoDTO prestamoDTO) {
        Prestamo nuevoPrestamo = new Prestamo();

        // Asignar el libro utilizando el ID del DTO
        Libro libro = libroRepository.findById(prestamoDTO.getId_libro())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el libro con el ID " + prestamoDTO.getId_libro()));

        // Asignar el usuario utilizando el ID del DTO
        Usuario usuario = usuarioRepository.findById(prestamoDTO.getId_usuario())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario con el ID " + prestamoDTO.getId_usuario()));

        // Configurar el préstamo
        nuevoPrestamo.setLibro(libro);
        nuevoPrestamo.setUsuario(usuario);
        nuevoPrestamo.setFecha_prestamo(prestamoDTO.getFecha_prestamo());

        // Establecer el estado predeterminado como "Prestado"
        nuevoPrestamo.setEstado("Prestado");

        // Guardar el préstamo en la base de datos
        Prestamo prestamoGuardado = prestamoRepository.save(nuevoPrestamo);

        return prestamoGuardado; // Retornar el préstamo guardado
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

        // Si el préstamo está activo, cambiar el estado del libro a "Disponible"
        if ("Prestado".equals(prestamo.getEstado())) {
            Libro libro = prestamo.getLibro(); // Obtiene el libro asociado
            if (libro != null) { // Verifica que el libro exista
                libro.setEstado("Disponible"); // Cambia el estado del libro a "Disponible"
                libroRepository.save(libro); // Guarda el cambio
            }
        }

        // Eliminar el préstamo
        prestamoRepository.delete(prestamo); // Elimina el préstamo
    }

    // Verificar si un usuario tiene préstamos asociados
    public boolean esPrestamoActivo(Long idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        return "Prestado".equals(prestamo.getEstado());
    }


    public boolean devolverPrestamo(Long idPrestamo, LocalDate fechaDevolucion) {
        // Busca el préstamo por su ID
        Optional<Prestamo> prestamoOpt = prestamoRepository.findById(idPrestamo);
        if (prestamoOpt.isPresent()) {
            Prestamo prestamo = prestamoOpt.get();
            prestamo.setFecha_devolucion(fechaDevolucion); // Actualiza la fecha de devolución
            prestamo.setEstado("Devuelto"); // Cambia el estado a "Devuelto"

            // Cambiar el estado del libro a "Disponible"
            Libro libro = prestamo.getLibro();
            libro.setEstado("Disponible");
            libroRepository.save(libro); // Guarda el cambio del libro

            prestamoRepository.save(prestamo); // Guarda el cambio del préstamo

            return true; // Indica que la operación fue exitosa
        } else {
            return false; // Indica que el préstamo no se encontró
        }
    }

    @Transactional
    public ResponseEntity<PrestamoDTO> modificarPrestamo(Long id, PrestamoDTO prestamoDTO) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo con ID: " + id + " no encontrado"));

        // Obtener el objeto Usuario antes de asignarlo al Prestamo
        if (prestamoDTO.getId_usuario() != null) {
            Usuario usuario = usuarioRepository.findById(prestamoDTO.getId_usuario())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            prestamo.setUsuario(usuario); // Establece el Usuario
        }

        // Obtener el objeto Libro antes de asignarlo al Prestamo
        if (prestamoDTO.getId_libro() != null) {
            Libro libro = libroRepository.findById(prestamoDTO.getId_libro())
                    .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
            prestamo.setLibro(libro); // Establece el Libro
        }

        // Otros campos del Prestamo
        if (prestamoDTO.getFecha_prestamo() != null) {
            prestamo.setFecha_prestamo(prestamoDTO.getFecha_prestamo());
        }

        if (prestamoDTO.getFecha_devolucion() != null) {
            prestamo.setFecha_devolucion(prestamoDTO.getFecha_devolucion());
        }

        if (prestamoDTO.getEstado() != null) {
            prestamo.setEstado(prestamoDTO.getEstado());
        }

        // Guardar el Prestamo modificado
        Prestamo prestamoModificado = prestamoRepository.save(prestamo);

        // Convertir a DTO para la respuesta
        PrestamoDTO resultado = convertirPrestamoDTO(prestamoModificado);

        return ResponseEntity.ok(resultado); // Respuesta exitosa
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


    /*

    private final PrestamoRepository prestamoRepository;
    private final UserRepository userRepository;
    private final LibroRepository libroRepository;

    public PrestamoDTO createPrestamo(PrestamoDTO prestamoDTO) {
        // Buscar Usuario por nombre
        Usuario usuario = userRepository.findByUsername(prestamoDTO.getUsuario().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + prestamoDTO.getUsuario().getUsername()));

        // Buscar Libro por título
        Libro libro = libroRepository.findByTitulo(prestamoDTO.getLibro().getTitulo())
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));

        if (!"Disponible".equalsIgnoreCase(libro.getEstado())) {
            throw new IllegalStateException("El libro no está disponible para préstamo.");
        }

        // Cambiar el estado del libro a 'Prestado'
        libro.setEstado("Prestado");
        libroRepository.save(libro);

              // Crear y guardar el Prestamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFecha_prestamo(prestamoDTO.getFecha_prestamo());
        prestamo.setFecha_devolucion(prestamoDTO.getFecha_devolucion());
        prestamo.setEstado("Prestado");

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);
        return prestamoAPrestamoDTO(prestamoGuardado);
    }



    public boolean devolverPrestamo(Long idPrestamo, LocalDate fechaDevolucion) {
        // Busca el préstamo por su ID
        Optional<Prestamo> prestamoOpt = prestamoRepository.findById(idPrestamo);
        if (prestamoOpt.isPresent()) {
            Prestamo prestamo = prestamoOpt.get();
            prestamo.setFecha_devolucion(fechaDevolucion); // Actualiza la fecha de devolución
            prestamo.setEstado("Devuelto"); // Cambia el estado a "Devuelto"

            // Cambiar el estado del libro a "Disponible"
            Libro libro = prestamo.getLibro();
            libro.setEstado("Disponible");
            libroRepository.save(libro); // Guarda el cambio del libro

            prestamoRepository.save(prestamo); // Guarda el cambio del préstamo

            return true; // Indica que la operación fue exitosa
        } else {
            return false; // Indica que el préstamo no se encontró
        }
    }

    // Verificar si un usuario tiene préstamos asociados
    public boolean tienePrestamosAsociados(Long idUsuario) {
        List<Prestamo> prestamos = prestamoRepository.findByUsuarioId(idUsuario);
        return !prestamos.isEmpty(); // Retorna verdadero si hay préstamos asociados
    }

    private PrestamoDTO prestamoAPrestamoDTO(Prestamo prestamo) {
        PrestamoDTO dto = new PrestamoDTO();
        dto.setId_prestamo(prestamo.getId_prestamo());
        dto.setUsuario(prestamo.getUsuario());
        dto.setLibro(prestamo.getLibro());
        dto.setFecha_prestamo(prestamo.getFecha_prestamo());
        dto.setFecha_devolucion(prestamo.getFecha_devolucion());
        dto.setEstado(prestamo.getEstado());
        return dto;
    }

    public List<PrestamoDTO> obtenerTodosLosPrestamos() {
        List<Prestamo> prestamos = prestamoRepository.findAll();
        return prestamos.stream()
                .map(this::prestamoAPrestamoDTO)
                .collect(Collectors.toList());
    }

    public PrestamoDTO obtenerPrestamoPorId(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prestamo no encontrado"));

        PrestamoDTO prestamoDTO = new PrestamoDTO();
        prestamoDTO.setId_prestamo(prestamo.getId_prestamo());
        prestamoDTO.setUsuario(prestamo.getUsuario());
        prestamoDTO.setLibro(prestamo.getLibro());
        prestamoDTO.setFecha_prestamo(prestamo.getFecha_prestamo());
        prestamoDTO.setFecha_devolucion(prestamo.getFecha_devolucion());
        prestamoDTO.setEstado(prestamo.getEstado());

        return prestamoDTO;
    }

    @Transactional
    public void eliminarPrestamo(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prestamo no encontrado"));

        // Si el préstamo está activo, cambiar el estado del libro a "Disponible"
        if ("Prestado".equals(prestamo.getEstado())) {
            Libro libro = prestamo.getLibro(); // Obtiene el libro asociado
            if (libro != null) { // Verifica que el libro exista
                libro.setEstado("Disponible"); // Cambia el estado del libro a "Disponible"
                libroRepository.save(libro); // Guarda el cambio
            }
        }

        // Eliminar el préstamo
        prestamoRepository.delete(prestamo); // Elimina el préstamo
    }

    public PrestamoDTO modificarPrestamo (Long id, PrestamoDTO prestamoDTO) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prestamo no encontrado"));

        prestamo.setUsuario(prestamoDTO.getUsuario());
        prestamo.setLibro(prestamoDTO.getLibro());
        prestamo.setFecha_prestamo(prestamoDTO.getFecha_prestamo());
        prestamo.setFecha_devolucion(prestamo.getFecha_devolucion());
        prestamo.setEstado(prestamo.getEstado());

        Prestamo prestamoModificado = prestamoRepository.save(prestamo);

        return prestamoAPrestamoDTO(prestamoModificado);
    }*/
}

