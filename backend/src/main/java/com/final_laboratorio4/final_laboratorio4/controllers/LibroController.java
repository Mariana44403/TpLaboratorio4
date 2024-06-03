package com.final_laboratorio4.final_laboratorio4.controllers;


import com.final_laboratorio4.final_laboratorio4.DTO.LibroDTO;
import com.final_laboratorio4.final_laboratorio4.models.Libro;
import com.final_laboratorio4.final_laboratorio4.services.LibroService;
import com.final_laboratorio4.final_laboratorio4.services.implementsService.ImplLibro;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/libro")
@RequiredArgsConstructor
public class LibroController {

    private final ImplLibro implLibro;

    @PostMapping
    public ResponseEntity<LibroDTO> crearLibro(@RequestBody LibroDTO libroDTO) {
        LibroDTO crearLibro = implLibro.crearLibro(libroDTO);
        return ResponseEntity.ok(crearLibro);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Libro>> buscarLibros(@RequestParam("query") String query) {
        List<Libro> librosFiltrados = implLibro.buscarLibros(query);
        return new ResponseEntity<>(librosFiltrados, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> obtenerLibroPorId(@PathVariable Long id) {
        LibroDTO libroDTO = implLibro.obtenerLibroPorId(id);
        return ResponseEntity.ok(libroDTO);
    }

    @GetMapping // Método HTTP para obtener todos los libros
    public ResponseEntity<List<LibroDTO>> obtenerTodosLosLibros() {
        List<LibroDTO> libros = implLibro.obtenerTodosLosLibros(); // Llama al servicio para obtener libros
        if (libros == null || libros.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 si no hay contenido
        }
        return ResponseEntity.ok(libros); // Devuelve 200 con la lista de libros
    }


    @PutMapping("/{id}")
    public ResponseEntity<LibroDTO> modificarLibro(
            @PathVariable Long id,
            @RequestBody LibroDTO libroDTO) {
        LibroDTO libroModificado = implLibro.modificarLibro(id, libroDTO);
        return ResponseEntity.ok(libroModificado);
    }


    @GetMapping("/{id}/tienePrestamos")
    public ResponseEntity<Boolean> tienePrestamosActivos(@PathVariable Long id) {
        boolean tienePrestamos = implLibro.tienePrestamosActivos(id);
        return ResponseEntity.ok(tienePrestamos);
    }

    // Eliminar libro si no tiene préstamos activos
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        try {
            implLibro.eliminarLibro(id);
            return ResponseEntity.noContent().build(); // Código 204: No Content
        } catch (IllegalStateException e) {
            // No se puede eliminar porque tiene préstamos activos
            return ResponseEntity.status(409).body(null); // Código 409: Conflict
        } catch (IllegalArgumentException e) {
            // Libro no encontrado
            return ResponseEntity.status(404).body(null); // Código 404: Not Found
        } catch (Exception e) {
            // Otros errores inesperados
            return ResponseEntity.status(500).body(null); // Código 500: Internal Server Error
        }
    }
}
