package learn.spring_security.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

  private final String secret = "examplerandoooomlargeeeeeeeeeeeeeSecureSecret";
  public String getToken(UserDetails user) {
    System.out.println(user.toString());
    System.out.println(user.getAuthorities());
    return getToken(new HashMap<>() , user);
  }

  private String getToken(Map<String, Object> extraClaims, UserDetails user) {
    return Jwts.builder()
      .setClaims(extraClaims)
      .setSubject(user.getUsername())
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + 60_000)) // 1 min
      .signWith(getKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  private Key getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String getUsernameFromToken(String token) {
    try {
      return getClaim(token, Claims::getSubject);
    } catch (ExpiredJwtException e) {
      System.out.println("EXPIRATION " + e.getMessage()); // Implementation of any logger for error logs
      return null;
    }
  }

  public boolean isValidToken(String token, UserDetails userDetails) {
    String username = getUsernameFromToken(token);
    return username.equals(userDetails.getUsername()) && !this.isTokenExpired(token);
  }

  private Claims getAllClaims(String token){
    return Jwts
      .parserBuilder()
      .setSigningKey(getKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public <T> T getClaim(String token, Function<Claims,T> claimsResolver) {
    final Claims claims=getAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Date getExpiration(String token) {
    return getClaim(token, Claims::getExpiration);
  }

  private boolean isTokenExpired(String token) {
    return getExpiration(token).before(new Date());
  }
}
