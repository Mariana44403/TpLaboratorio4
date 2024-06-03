package com.final_laboratorio4.final_laboratorio4.repositories;

import com.final_laboratorio4.final_laboratorio4.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {
    // Método para encontrar un usuario por su nombre de usuario
    Optional<Usuario> findByUsername(String username);  // Usar el nombre correcto

    Optional<Usuario> findById(Long id);

    @Query("SELECT u FROM Usuario u " +
            "WHERE LOWER(u.username) LIKE LOWER(:query)||'%' " +
            "OR LOWER(u.firstname) LIKE LOWER(:query)||'%' " +
            "OR LOWER(u.lastname) LIKE LOWER(:query)||'%' " +
            "OR LOWER(u.phone) LIKE LOWER(CONCAT(:query, '%')) " +
            "OR LOWER(u.role) LIKE LOWER(CONCAT(:query, '%'))")
    List<Usuario> buscarPorQuery(@Param("query") String query);

}
