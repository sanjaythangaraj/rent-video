package com.example.rent_video.service.auth;

import com.example.rent_video.exchange.auth.LoginRequest;
import com.example.rent_video.exchange.auth.LoginResponse;
import com.example.rent_video.exchange.auth.RegisterRequest;
import com.example.rent_video.exchange.auth.RegisterResponse;
import com.example.rent_video.exchange.customer.CustomerRequest;
import com.example.rent_video.exchange.customer.CustomerResponse;
import com.example.rent_video.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JWTService jwtService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public RegisterResponse register(RegisterRequest registerRequest) {
    CustomerRequest customerRequest = modelMapper.map(registerRequest, CustomerRequest.class);
    String hashPwd = passwordEncoder.encode(customerRequest.getPassword());
    customerRequest.setPassword(hashPwd);
    if (customerRequest.getRole() == null) customerRequest.setRole("customer");
    customerRequest.setRole("ROLE_" + customerRequest.getRole().toUpperCase());
    CustomerResponse customerResponse = customerService.save(customerRequest);
    return modelMapper.map(customerResponse, RegisterResponse.class);
  }

  private Authentication authenticate(String username, String password) {
    Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    return authenticationManager.authenticate(authentication);
  }

  private String authenticateAndGetJWT(String username, String password) {
    Authentication authentication = authenticate(username, password);
    if (authentication != null && authentication.isAuthenticated()) {
      return jwtService.getJWT(authentication);
    }
    return null;
  }

  public LoginResponse login(LoginRequest loginRequest) {
    String jwt = authenticateAndGetJWT(loginRequest.email(), loginRequest.password());
    if (jwt == null) jwt = "";
    return new LoginResponse(HttpStatus.OK.getReasonPhrase(), jwt);
  }
}
