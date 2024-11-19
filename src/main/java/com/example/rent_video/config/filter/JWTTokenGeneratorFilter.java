package com.example.rent_video.config.filter;

import com.example.rent_video.service.auth.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.rent_video.constants.ApplicationConstants.JWT_HEADER;

@Component
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

  @Autowired
  private JWTService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      String jwt = jwtService.getJWT(authentication);
      if (jwt != null) response.setHeader(JWT_HEADER, jwt);
    }

    filterChain.doFilter(request, response);
  }
}
