package com.danielbohry.authservice.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

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

    public Authentication generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(
            "authorities", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toSet())
        );
        return generateToken(claims, userDetails);
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Authentication generateToken(Map<String, Object> claims, UserDetails userDetails) {
        Date expirationDate = new Date(currentTimeMillis() + 1000 * 60 * 60 * 48);
        String token = Jwts.builder().setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(currentTimeMillis()))
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS256, secret).compact();

        return new Authentication(token,
            expirationDate.toInstant(),
            userDetails.getUsername(),
            userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token [{}]", e.getClaims());
            return e.getClaims();
        }
    }

}
