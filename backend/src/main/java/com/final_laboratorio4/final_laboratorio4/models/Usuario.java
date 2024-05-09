package com.final_laboratorio4.final_laboratorio4.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Data // Genera automaticamente getters, setters, y otros metodos utiles
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
@Builder // Permite utilizar el patron de constructor para crear instancias de la clase de manera fluida
@Entity
// Establece una restriccion unica para la columna "username", q cada usuario tenga un nombre de usuario unico
@Table(name = "usuario", uniqueConstraints = {@UniqueConstraint(columnNames = ("username"))})
public class Usuario implements UserDetails {  // UserDetails permite usar la clase Usuario para autenticacion y autorizacion
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Integer id;
    private Integer id;
    @Column(nullable = false)
    private String username;
    private String firstname;
    private String lastname;
    // String country;
    private String phone;
    private String password; // Debe ser cifrada para seguridad
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Devuelve las autoridades del usuario. Basadas en el rol del usuario
        return List.of(new SimpleGrantedAuthority((role.name())));
    }

    // Devuelve la contraseña del usuario
    @Override
    public String getPassword() {
        return password;
    }

    // Devuelve el nombre del usuario
    @Override
    public String getUsername() {
        return username;
    }

    // Retorna true, si la cuenta no ha expirado.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Retorna true, si la cuenta no esta bloqueada
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Retorna true, si las credenciales (contraseña) no han expirado
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Retorna true, si la cuenta esta habilitada.
    @Override
    public boolean isEnabled() {
        return true;
    }

}
