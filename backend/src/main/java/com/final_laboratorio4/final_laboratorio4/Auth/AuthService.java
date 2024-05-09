package com.final_laboratorio4.final_laboratorio4.Auth;
import com.final_laboratorio4.final_laboratorio4.JWT.JwtService;
import com.final_laboratorio4.final_laboratorio4.models.Role;
import com.final_laboratorio4.final_laboratorio4.repositories.UserRepository;
import com.final_laboratorio4.final_laboratorio4.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    /* Dependencias */
    private final AuthenticationManager authenticationManager; // Autentica credenciales de un usuario
    private final JwtService jwtService; // Servicio q genera y valida tokens de autenticacion
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Componente para cifrar contraseñas

    public AuthResponse login(LoginRequest request) {
        // Autentica las credenciales (usuario y contraseña) proporcionadas en LoginRequest
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        //  Busca el usuario
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        // Si la autenticacion es exitosa, genera un token JWT
        String token = jwtService.getToken(user);
        // Retorna el token generado
        return AuthResponse.builder()
                .token(token)
                .build();
    }


    public AuthResponse register(RegisterRequest request) {
        // Verifica si ya existe un usuario con el mismo nombre, si existe lanza una excepcion
        Optional<Usuario> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isPresent()) {
            throw new RuntimeException("Ya existe ese usuario");
        }

        // Si el usuario no existe, lo crea con la informacion de RegisterRequest
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // Cifra la contraseña
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .role(Role.ADMINISTRADOR)
                .build();

        // Guarda el usuario
        userRepository.save(usuario);
        // Genera y retorna el token
        return AuthResponse.builder()
                .token(jwtService.getToken(usuario))
                .build();
    }
}
