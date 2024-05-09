package com.final_laboratorio4.final_laboratorio4.DTO;

import com.final_laboratorio4.final_laboratorio4.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {
    private Integer id;
    private String username;
    private String firstname;
    private String lastname;
    private String phone;
    private Role role;
}