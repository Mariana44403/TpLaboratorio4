package com.final_laboratorio4.final_laboratorio4.repositories;

import com.final_laboratorio4.final_laboratorio4.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {
    // Método para encontrar un usuario por su nombre de usuario
    Optional<Usuario> findByUsername(String username);  // Usar el nombre correcto

    Optional<Usuario> findById(Long id);
}
