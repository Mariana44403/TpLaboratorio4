package com.final_laboratorio4.final_laboratorio4.controllers;

import com.final_laboratorio4.final_laboratorio4.DTO.PrestamoDTO;
import com.final_laboratorio4.final_laboratorio4.models.Prestamo;
import com.final_laboratorio4.final_laboratorio4.repositories.PrestamoRepository;
import com.final_laboratorio4.final_laboratorio4.services.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/prestamo")
public class PrestamoController {
    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private PrestamoService prestamoService;
    @GetMapping
    public List<Prestamo> getAllPrestamo() {
        return prestamoRepository.findAll();
    }
    @GetMapping("/conIds")
    public List<PrestamoDTO> getAllPrestamos(){
        return prestamoService.getAllPrestamos();
    }
    @GetMapping("/{id}")
    public PrestamoDTO getPrestamoById(@PathVariable Long id) {
        return prestamoService.getPrestamoById(id);
    }
    @PostMapping
    public Prestamo createPrestamo(@RequestBody PrestamoDTO prestamoDTO){
        Prestamo nuevoPrestamo = prestamoService.CrearPrestamo(prestamoDTO);
        return prestamoRepository.save(nuevoPrestamo);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        try {
            prestamoService.eliminarPrestamo(id); // Llama al servicio para eliminar el préstamo
            return ResponseEntity.noContent().build(); // Devuelve respuesta HTTP 204 (No Content)
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null); // Devuelve respuesta HTTP 404 si el préstamo no se encuentra
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Devuelve respuesta HTTP 500 para errores inesperados
        }
    }

    @GetMapping("/{id}/activo")
    public ResponseEntity<Boolean> esPrestamoActivo(@PathVariable Long id) {
        try {
            boolean activo = prestamoService.esPrestamoActivo(id);
            return ResponseEntity.ok(activo);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // El préstamo no fue encontrado
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<PrestamoDTO> modificarPrestamo(
            @PathVariable Long id,
            @RequestBody PrestamoDTO prestamoDTO
    ) {
        // Llamar al servicio para modificar el préstamo
        ResponseEntity<PrestamoDTO> response = prestamoService.modificarPrestamo(id, prestamoDTO);

        return response; // Devolver la respuesta del servicio
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolverPrestamo(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        LocalDate fechaDevolucion = LocalDate.parse(body.get("fecha_devolucion"));

        boolean resultado = prestamoService.devolverPrestamo(id, fechaDevolucion);
        if (resultado) {
            return ResponseEntity.ok("Préstamo devuelto con éxito.");
        } else {
            return ResponseEntity.status(404).body("Préstamo no encontrado.");
        }
    }
    /*
    @Autowired
    private PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<PrestamoDTO> createPrestamo(@RequestBody PrestamoDTO prestamoDTO) {
        try {
            PrestamoDTO nuevoPrestamo = prestamoService.createPrestamo(prestamoDTO);
            return ResponseEntity.ok(nuevoPrestamo); // Devuelve el nuevo préstamo si todo sale bien
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Respuesta 400 si hay un problema
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Respuesta 500 para otros errores inesperados
        }
    }

    @GetMapping
    public ResponseEntity<List<PrestamoDTO>> obtenerTodosLosPrestamos() {
        List<PrestamoDTO> prestamos = prestamoService.obtenerTodosLosPrestamos();
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/{id}/activo")
    public ResponseEntity<Boolean> esPrestamoActivo(@PathVariable Long id) {
        try {
            boolean activo = prestamoService.esPrestamoActivo(id);
            return new ResponseEntity<>(activo, HttpStatus.OK); // Devuelve true o false
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Préstamo no encontrado");
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        try {
            prestamoService.eliminarPrestamo(id); // Llama al servicio para eliminar el préstamo
            return ResponseEntity.noContent().build(); // Devuelve respuesta HTTP 204 (No Content)
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null); // Devuelve respuesta HTTP 404 si el préstamo no se encuentra
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Devuelve respuesta HTTP 500 para errores inesperados
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoDTO> obtenerPrestamoPorId(@PathVariable Long id) {
        PrestamoDTO prestamoDTO = prestamoService.getPrestamoById(id);
        return ResponseEntity.ok(prestamoDTO);
    }

    @PutMapping
    public ResponseEntity<String> modificarPrestamo(@RequestBody PrestamoDTO prestamoDTO) {
        if (prestamoDTO.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID del préstamo es requerido");
        }

        return prestamoService.modificarPrestamo(prestamoDTO);
    }


    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolverPrestamo(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        LocalDate fechaDevolucion = LocalDate.parse(body.get("fecha_devolucion"));

        boolean resultado = prestamoService.devolverPrestamo(id, fechaDevolucion);
        if (resultado) {
            return ResponseEntity.ok("Préstamo devuelto con éxito.");
        } else {
            return ResponseEntity.status(404).body("Préstamo no encontrado.");
        }
    }*/
}
