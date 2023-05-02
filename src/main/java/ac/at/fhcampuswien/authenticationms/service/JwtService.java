package ac.at.fhcampuswien.authenticationms.service;


import ac.at.fhcampuswien.authenticationms.exceptions.CustomerNotFoundException;
import ac.at.fhcampuswien.authenticationms.exceptions.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    //256-bit encryption for key
    public static final String SECRET = "58703273357638792F423F4528472B4B6250655368566D597133743677397A24";

    @Autowired
    CustomerRepository customerRepository;

    public enum Token {
        AccessToken,
        RefreshToken
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public void isTokenExpiredOrInvalid(String token) throws InvalidTokenException, CustomerNotFoundException {
        if (extractExpiration(token).before(new Date())) {
            throw new InvalidTokenException("Token is expired.");
        }
        validateToken(token);
    }

    public void validateToken(String token) throws CustomerNotFoundException {
        String userEmail = extractUserEmail(token);

        if (!customerRepository.existsByeMail(userEmail)) {
            throw new CustomerNotFoundException("Token is invalid.");
        }
    }

    public String generateToken(String userEmail, Token token) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userEmail, token);
    }

    private String createToken(Map<String, Object> claims, String userEmail, Token token) {
        int expirationTimeInMillis = 0;

        switch(token) {
            case AccessToken -> expirationTimeInMillis = 60 * 5; // 5 Minuten
            case RefreshToken -> expirationTimeInMillis = 60 * 60 * 24; // 1 Tag
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userEmail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * expirationTimeInMillis))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}


