package com.final_laboratorio4.final_laboratorio4.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Dependencias
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    // Se ejecuta para cada solicitud HTTP, verifica y autentica usando JWT
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Obtener el token del request
        final String token = getTokenFromRequest(request);
        final String username;
        if(token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // Si no encuentra un token, extrae el nombre del usuario
        username = jwtService.getUsernameFromToken(token);
        // Si el nombre de usuario no es null y no hay autenticacion existente, se autentica.
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Se carga el UserDetails para ese nombre de usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // Verifica si el token es valido para ese usuario
            if(jwtService.isTokenValid(token, userDetails)) {
                // Si el token es valido se crea UsernamePasswordAuthenticationToken
                // Con el nombre del usuario, null para la contraseña (ya q se usa el token para autenticacion) y las autoridades del usuario
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                // El authentication se configura con detalles adicionales
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Se establece la autenticacion
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // Una vez finalizada la autenticacion, el filtro continua con el siguiente filtro
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // Obtiene el token JWT de la cabecera Authorization
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        // Comprueba que la cabecera no sea null, y q comience con "Bearer".
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            // Si es asi, extrae el token eliminando el prefijo "Bearer"
            return authHeader.substring(7); // Obtienes solo el token JWT
        }
        // Si la cabecera no es valida, devuelve null
        return null;
    }
}
