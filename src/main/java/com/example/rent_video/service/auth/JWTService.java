package com.example.rent_video.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static com.example.rent_video.constants.ApplicationConstants.JWT_SECRET_DEFAULT_VALUE;
import static com.example.rent_video.constants.ApplicationConstants.JWT_SECRET_KEY;

@Service
public class JWTService {

  @Autowired
  private Environment env;

  private SecretKey getSecretKey() {
    if (env != null) {
      String secret = env.getProperty(JWT_SECRET_KEY, JWT_SECRET_DEFAULT_VALUE);
      return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    return null;
  }

  public String getJWT(Authentication authentication) {
    SecretKey secretKey = getSecretKey();
    if (secretKey != null) {
      return Jwts.builder().issuer("rent-video-api").subject("JWT Token")
          .claim("username", authentication.getName())
          .claim("authorities", authentication.getAuthorities().stream().map(
              GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
          .issuedAt(new java.util.Date())
          .expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
          .signWith(secretKey).compact();
    }

    return null;
  }

  public void validateJWT(String jwt) {
    SecretKey secretKey = getSecretKey();
    if (secretKey != null) {
      try {
        Claims claims = Jwts.parser().verifyWith(secretKey)
            .build().parseSignedClaims(jwt).getPayload();
        String username = String.valueOf(claims.get("username"));
        String authorities = String.valueOf(claims.get("authorities"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (Exception exception) {
        throw new BadCredentialsException("Invalid Token received!");
      }
    }
  }
}
