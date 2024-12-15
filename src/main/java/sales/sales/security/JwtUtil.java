package sales.sales.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
    
@Component
public class JwtUtil {

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("secret".getBytes());

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Token valid 1 hari
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // Gunakan Key di sini
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Gunakan Key untuk validasi
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Metode untuk mendapatkan waktu kedaluwarsa
    public Date getExpiration(String token) {
        Claims claims = validateToken(token); // Gunakan validateToken untuk mendapatkan Claims
        return claims.getExpiration(); // Ambil waktu expired dari Claims
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Metode untuk mendapatkan Authentication dari token
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token); // Ambil username dari Claims
        String username = claims.getSubject();

        User user = new User(username, "", new ArrayList<>()); // Buat User kosong dengan username
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
