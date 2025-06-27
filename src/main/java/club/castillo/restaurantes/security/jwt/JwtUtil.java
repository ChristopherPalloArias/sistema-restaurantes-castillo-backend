package club.castillo.restaurantes.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMs = 86400000; // 1 day

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .orElse("USER");

        // Extraer el nombre del usuario del email
        String email = userDetails.getUsername();
        System.out.println("Email del usuario: " + email);
        
        // Extraer y formatear el nombre del email
        String name;
        try {
            String nameFromEmail = email.substring(0, email.indexOf('@'));
            System.out.println("Nombre antes de formatear: " + nameFromEmail);
            
            // Reemplazar puntos por espacios y dividir en palabras
            String[] words = nameFromEmail.replace('.', ' ').split("\\s+");
            StringBuilder nameBuilder = new StringBuilder();
            
            // Capitalizar cada palabra
            for (String word : words) {
                if (!word.isEmpty()) {
                    nameBuilder.append(word.substring(0, 1).toUpperCase())
                            .append(word.substring(1).toLowerCase())
                            .append(" ");
                }
            }
            name = nameBuilder.toString().trim();
            System.out.println("Nombre formateado: " + name);
            
        } catch (Exception e) {
            System.err.println("Error formateando el nombre: " + e.getMessage());
            name = email.substring(0, email.indexOf('@')); // Fallback al nombre sin formatear
        }

        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)
                .claim("name", name)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("Token generado con nombre: " + name);
        return token;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
