package com.final_laboratorio4.final_laboratorio4.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}") // Inyecta el valor 'jwt.secret' que se usa como clave secreta para firmar JWT
    private String SECRET_KEY; // Clave secreta para firmar y validar JWT

    // Genera un token JWT, se pasan reclamaciones vacias
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    // Genera un token JWT con reclamaciones adicionales
    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*86400))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Obtiene la clave para firmar el JWT
    private Key getKey() {
        byte[] keybytes = Decoders.BASE64.decode(SECRET_KEY); // Decodifica la clave secreta
        return Keys.hmacShaKeyFor(keybytes); // Crea una clave HMAC-SHA256 a partir de los bytes decodificados
    }

    // Extrae el nombre del usuario del JWT.
    public String getUsernameFromToken(String token) {
        return getClain(token, Claims::getSubject);
    }

    // Verifica si el JWT es valido para un usuario especifico.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        // Comprueba q el nombre del usuario en el JWT coincide con el nombre del usuario del UserDetails
        // Y q el token no ha expirado
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Obtiene todas las reclamaciones del JWT
    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extrae una reclamacion especifica del JWT
    public <T> T getClain(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims); // el claimsResolver permite obtener partes especificar del JWT
    }

    // Obtiene la fecha de expiracion del JWT
    private Date getExpiration(String token) {
        return getClain(token, Claims::getExpiration);
    }

    // Comprueba si el JWT ha expirado, llamando al get Expiration
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
