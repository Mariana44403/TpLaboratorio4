package com.final_laboratorio4.final_laboratorio4.controllers;

import com.final_laboratorio4.final_laboratorio4.DTO.UsuarioDTO;
import com.final_laboratorio4.final_laboratorio4.models.Usuario;
import com.final_laboratorio4.final_laboratorio4.services.PrestamoService;
import com.final_laboratorio4.final_laboratorio4.services.UsuarioService;
import com.final_laboratorio4.final_laboratorio4.services.implementsService.ImplPrestamo;
import com.final_laboratorio4.final_laboratorio4.services.implementsService.ImplUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final ImplUsuario implUsuario;
    private final ImplPrestamo implPrestamo;

    @PostMapping
    public ResponseEntity<Usuario> createUser(@RequestBody UsuarioDTO userDTO) {
        Usuario newUser = implUsuario.createUser(userDTO);
        return ResponseEntity.ok(newUser); // Devuelve el usuario recién creado
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsers() {
        List<Usuario> usuarios = implUsuario.getAllUsers();
        return ResponseEntity.ok(usuarios); // Devuelve todos los usuarios
    }

    @GetMapping("/search")
    public ResponseEntity<List<Usuario>> buscarUsuarios(@RequestParam("query") String query) {
        List<Usuario> usuariosFiltrados = implUsuario.buscarPorQuery(query);
        return new ResponseEntity<>(usuariosFiltrados, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> modificarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioModificado = implUsuario.modificarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioModificado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioDTO usuarioDTO = implUsuario.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuarioDTO);
    }

    @GetMapping("/{id}/tienePrestamosActivos")
    public ResponseEntity<Boolean> tienePrestamosActivos(@PathVariable Long id) {
        boolean tienePrestamosActivos = implUsuario.tienePrestamosActivos(id);
        return ResponseEntity.ok(tienePrestamosActivos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            implUsuario.eliminarUsuario(id);
            return ResponseEntity.ok().build(); // Respuesta de éxito
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
