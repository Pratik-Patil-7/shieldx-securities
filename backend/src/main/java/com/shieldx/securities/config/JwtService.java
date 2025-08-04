//package com.shieldx.securities.config;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//
//@Service
//public class JwtService {
//
//	private final Key key;
//	private final long expirationMs;
//
//	public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationSeconds) {
//		byte[] keyBytes = Decoders.BASE64.decode(secret);
//		this.key = Keys.hmacShaKeyFor(keyBytes);
//		this.expirationMs = expirationSeconds * 1000; // Convert to milliseconds
//	}
//
//	public String generateToken(Integer userId) {
//		return Jwts.builder().setSubject(userId.toString()) // Store as String in token
//				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expirationMs))
//				.signWith(key, SignatureAlgorithm.HS256).compact();
//	}
//
//	public String extractUserId(String token) {
//		return extractClaim(token, Claims::getSubject); // Returns String
//	}
//
//	public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
//		Claims claims = extractAllClaims(token);
//		return claimsResolver.apply(claims);
//	}
//
//	private Claims extractAllClaims(String token) {
//		try {
//			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//		} catch (Exception e) {
//			throw new IllegalArgumentException("Invalid JWT token", e);
//		}
//	}
//
//	public boolean isTokenValid(String token, String userId) {
//		try {
//			String extractedUserId = extractUserId(token);
//			return extractedUserId.equals(userId) && !isTokenExpired(token);
//		} catch (Exception e) {
//			return false;
//		}
//	}
//
//	private boolean isTokenExpired(String token) {
//		return extractExpiration(token).before(new Date());
//	}
//
//	private Date extractExpiration(String token) {
//		return extractClaim(token, Claims::getExpiration);
//	}
//}


package com.shieldx.securities.config;

import com.shieldx.securities.model.Login;
import com.shieldx.securities.repository.LoginRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final Key key;
    private final long expirationMs;
    private final LoginRepository loginRepository;

    public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationSeconds,
                      LoginRepository loginRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationSeconds * 1000;
        this.loginRepository = loginRepository;
    }

    public String generateToken(Integer userId) {
        Map<String, Object> claims = new HashMap<>();
        Login login = loginRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found for token generation: " + userId));
        claims.put("email", login.getEmail());
        claims.put("role", login.getEmail().toLowerCase().endsWith("@shieldx.com") ? "ADMIN" : "USER");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public boolean isTokenValid(String token, String userId) {
        try {
            String extractedUserId = extractUserId(token);
            return extractedUserId.equals(userId) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}