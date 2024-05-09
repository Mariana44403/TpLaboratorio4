package com.final_laboratorio4.final_laboratorio4.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "login")
    // Recibe un LoginRequest con usuario y contraseña para autenticar
    public ResponseEntity<AuthResponse> login (@RequestBody LoginRequest request) {
        try {
            // Si la autenticacion es exitosa devuelve el token
            return ResponseEntity.ok(authService.login(request));
        } catch (RuntimeException e) {
            // Si no, retorna un mensaje de error
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "register")
    // Recibe un registerRequest con los datos para registrar un nuevo usuario
    public ResponseEntity<AuthResponse> register (@RequestBody RegisterRequest request) {
        try {
            // Si el registro es exitoso devuelve el token JWT
            return ResponseEntity.ok(authService.register(request));
        } catch (RuntimeException e) {
            // Si no, retorna un mensaje de error
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
