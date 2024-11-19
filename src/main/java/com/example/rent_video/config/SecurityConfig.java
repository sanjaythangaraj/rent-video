package com.example.rent_video.config;

import com.example.rent_video.config.filter.JWTTokenGeneratorFilter;
import com.example.rent_video.config.filter.JWTTokenValidatorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {

  @Autowired
  JWTTokenGeneratorFilter jwtTokenGeneratorFilter;

  @Autowired
  JWTTokenValidatorFilter jwtTokenValidatorFilter;

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    httpSecurity.addFilterAfter(jwtTokenGeneratorFilter, BasicAuthenticationFilter.class);
    httpSecurity.addFilterBefore(jwtTokenValidatorFilter, BasicAuthenticationFilter.class);

    httpSecurity.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
        .requestMatchers(antMatcher(HttpMethod.GET,"/api/videos/{id}")).hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers(HttpMethod.GET,"/api/videos").hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers(HttpMethod.POST,"/api/videos").hasAnyRole("ADMIN")
        .requestMatchers(antMatcher(HttpMethod.DELETE,"/api/videos/{id}")).hasAnyRole("ADMIN")
        .requestMatchers(antMatcher(HttpMethod.PATCH,"/api/videos/{id}")).hasAnyRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/customers").hasAnyRole("ADMIN")
        .requestMatchers(antMatcher("/api/customers/{id}")).hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers(antMatcher("/api/customers/{id}/videos")).hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers(antMatcher("/api/videos/{id}/rent")).hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers(antMatcher("/api/videos/{id}/return")).hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers("/api/login", "/api/register").permitAll());

    return httpSecurity.build();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
}
