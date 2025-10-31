package products.demo.Security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET= "hellobbieyellowchaloooooo!@#$%^&*()";
    private static final long EXPIRATION_TIME=1000*60*60;

    private final Key key= Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username, String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

    public String extractUserName(String token){
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token){
        return (String) extractClaims(token).get("role");
    }

    public boolean isTokenValid(String token, String userName){
        return userName.equals(extractUserName(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

}
