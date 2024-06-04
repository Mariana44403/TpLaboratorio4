package com.final_laboratorio4.final_laboratorio4.controllers;

import com.final_laboratorio4.final_laboratorio4.DTO.PrestamoDTO;
import com.final_laboratorio4.final_laboratorio4.models.Libro;
import com.final_laboratorio4.final_laboratorio4.models.Prestamo;
import com.final_laboratorio4.final_laboratorio4.repositories.PrestamoRepository;
import com.final_laboratorio4.final_laboratorio4.services.PrestamoService;
import com.final_laboratorio4.final_laboratorio4.services.implementsService.ImplPrestamo;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoRepository prestamoRepository;
    private final ImplPrestamo implPrestamo;

    @PostMapping
    public Prestamo createPrestamo(@RequestBody PrestamoDTO prestamoDTO){
        Prestamo nuevoPrestamo = implPrestamo.crearPrestamo(prestamoDTO);
        return prestamoRepository.save(nuevoPrestamo);
    }

    @GetMapping
    public List<Prestamo> getAllPrestamo() {
        return prestamoRepository.findAll();
    }

    @GetMapping("/conIds")
    public List<PrestamoDTO> getAllPrestamos(){
        return implPrestamo.getAllPrestamos();
    }

    @GetMapping("/{id}")
    public PrestamoDTO getPrestamoById(@PathVariable Long id) {
        return implPrestamo.getPrestamoById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Prestamo>> buscarPrestamo(@RequestParam("query") String query) {
        List<Prestamo> prestamosFiltrados = implPrestamo.buscarPorQuery(query);
        return new ResponseEntity<>(prestamosFiltrados, HttpStatus.OK);
    }

    @GetMapping("/{id}/activo")
    public ResponseEntity<Boolean> esPrestamoActivo(@PathVariable Long id) {
        try {
            boolean activo = implPrestamo.esPrestamoActivo(id);
            return ResponseEntity.ok(activo);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        try {
            implPrestamo.eliminarPrestamo(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrestamoDTO> modificarPrestamo(@PathVariable Long id, @RequestBody PrestamoDTO prestamoDTO) {
        ResponseEntity<PrestamoDTO> response = implPrestamo.modificarPrestamo(id, prestamoDTO);
        return response;
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolverPrestamo(@PathVariable Long id, @RequestBody Map<String, String> body) {
        LocalDate fechaDevolucion = LocalDate.parse(body.get("fecha_devolucion"));

        boolean resultado = implPrestamo.devolverPrestamo(id, fechaDevolucion);
        if (resultado) {
            return ResponseEntity.ok("Préstamo devuelto con éxito.");
        } else {
            return ResponseEntity.status(404).body("Préstamo no encontrado.");
        }
    }
}
