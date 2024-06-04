package com.final_laboratorio4.final_laboratorio4.services.implementsService;

import com.final_laboratorio4.final_laboratorio4.DTO.UsuarioDTO;
import com.final_laboratorio4.final_laboratorio4.models.Usuario;
import org.hibernate.id.uuid.UuidGenerator;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ImplUsuario{
    Usuario createUser(UsuarioDTO usuarioDTO);

    List<Usuario> getAllUsers();

    UsuarioDTO obtenerUsuarioPorId(Long id);

    UsuarioDTO modificarUsuario(Long id, UsuarioDTO usuarioDTO);

    boolean tienePrestamosActivos(Long idUsuario);

    ResponseEntity<String> eliminarUsuario(Long id);

    List<Usuario> buscarPorQuery(String query);
}
