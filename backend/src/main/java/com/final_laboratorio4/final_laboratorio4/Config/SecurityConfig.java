package com.final_laboratorio4.final_laboratorio4.Config;

import com.final_laboratorio4.final_laboratorio4.JWT.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // Dependencias
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                .csrf(csrf ->
                        csrf
                        .disable()) // Desactivar CSRF si usas JWT
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                // Permite acceso abierto a rutas de q comienzan con "/auth/". Esto es para permitir el registro y el inicio de seion sin autenticacion previa
                                .requestMatchers("/auth/**").permitAll()
                                // Requiere autenticacion para todas las demas solicitudes.
                                .anyRequest().authenticated() // Cualquier ruta fuera de "/auth/" necesitara un tokent JWT valido
                )
                // Indica q la aplicacion no mantendra sesiones
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configurar la política de creación de sesiones como stateless
                )
                .authenticationProvider(authProvider) // Configurar el AuthenticationProvider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Añadir el filtro JWT antes del filtro de autenticación por contraseña
                .build(); // Compila y devuelve la cadena de filtros de seguridad configurada
    }
}
