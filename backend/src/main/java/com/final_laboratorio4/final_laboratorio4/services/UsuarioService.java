package com.final_laboratorio4.final_laboratorio4.services;

import com.final_laboratorio4.final_laboratorio4.DTO.UsuarioDTO;
import com.final_laboratorio4.final_laboratorio4.models.Role;
import com.final_laboratorio4.final_laboratorio4.repositories.UserRepository;
import com.final_laboratorio4.final_laboratorio4.models.Usuario;
import com.final_laboratorio4.final_laboratorio4.models.Prestamo;
import com.final_laboratorio4.final_laboratorio4.repositories.LibroRepository;
import com.final_laboratorio4.final_laboratorio4.repositories.PrestamoRepository;
import com.final_laboratorio4.final_laboratorio4.services.implementsService.ImplUsuario;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService implements ImplUsuario {
    // Dependencias
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public Usuario createUser(UsuarioDTO usuarioDTO) {
        Usuario usuario = Usuario.builder()
                .username(usuarioDTO.getUsername())
                .firstname(usuarioDTO.getFirstname())
                .lastname(usuarioDTO.getLastname())
                .phone(usuarioDTO.getPhone())
                .role(Role.USUARIO)
                .build();
        return userRepository.save(usuario);
    }

    public UsuarioDTO usuarioAUsuarioDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setFirstname(usuario.getFirstname());
        usuarioDTO.setLastname(usuario.getLastname());
        usuarioDTO.setPhone(usuario.getPhone());
        return usuarioDTO;
    }

    public List<Usuario> getAllUsers() {
        return userRepository.findAll();
    }


    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setFirstname(usuario.getFirstname());
        usuarioDTO.setLastname(usuario.getLastname());
        usuarioDTO.setPhone(usuario.getPhone());

        return usuarioDTO;
    }

    public UsuarioDTO modificarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setFirstname(usuarioDTO.getFirstname());
        usuario.setLastname(usuarioDTO.getLastname());
        usuario.setPhone(usuarioDTO.getPhone());

        Usuario usuarioModificado = userRepository.save(usuario);

        return usuarioAUsuarioDTO(usuarioModificado);
    }

    public boolean tienePrestamosActivos(Long idUsuario) {
        List<Prestamo> prestamos = prestamoRepository.findByUsuarioId(idUsuario);
        return prestamos.stream().anyMatch(prestamo -> "Prestado".equals(prestamo.getEstado()));
    }

    @Transactional
    public ResponseEntity<String> eliminarUsuario(Long id) {
        try {
            Usuario usuario = userRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("Usuario no encontrado"));

            if (tienePrestamosActivos(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuario tiene préstamos activos. No se puede eliminar.");
            }


            List<Prestamo> prestamos = prestamoRepository.findByUsuarioId(id);
            for (Prestamo prestamo : prestamos) {
                prestamo.setUsuario(null);
                prestamoRepository.save(prestamo);
            }

            userRepository.delete(usuario);

            return ResponseEntity.ok("Usuario eliminado con éxito.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> buscarPorQuery(String query) {
        return userRepository.buscarPorQuery(query);
    }
}
