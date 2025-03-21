package authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// import javax.crypto.SecretKey;

@Service
public class JwtService {

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // private Claims extractAllClaims(String token) {
    // return Jwts.parser() // ✅ Use `parser()` instead of `parserBuilder()`
    // .verifyWith((SecretKey) getSignKey()) // ✅ Cast to SecretKey
    // .build()
    // .parseSignedClaims(token)
    // .getPayload(); // ✅ Updated method to get claims
    // }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // ✅ Corrected to parserBuilder()
                .setSigningKey(getSignKey()) // ✅ Use setSigningKey instead of verifyWith
                .build()
                .parseClaimsJws(token) // ✅ Corrected method
                .getBody(); // ✅ Corrected to getBody()
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String GenerateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // private String createToken(Map<String, Object> claims, String username) {
    // return Jwts.builder()
    // .claims(claims) // Updated method
    // .subject(username) // Updated method
    // .issuedAt(new Date(System.currentTimeMillis()))
    // .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1))
    // .signWith(getSignKey()) // Remove SignatureAlgorithm, it's inferred from Key
    // .compact();
    // }
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims) // ✅ Corrected method
                .setSubject(username) // ✅ Corrected method
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 minutes expiration
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // ✅ Corrected signWith()
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
