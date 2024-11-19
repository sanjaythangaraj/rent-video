package com.example.rent_video.service;

import com.example.rent_video.data.AuthorityEntity;
import com.example.rent_video.data.CustomerEntity;
import com.example.rent_video.exception.customer.CustomerAlreadyExistsException;
import com.example.rent_video.exception.customer.CustomerNotFoundException;
import com.example.rent_video.exception.customer.UnauthorizedAccessException;
import com.example.rent_video.exchange.customer.CustomerRequest;
import com.example.rent_video.exchange.customer.CustomerResponse;
import com.example.rent_video.exchange.video.VideoResponse;
import com.example.rent_video.repository.AuthorityRepository;
import com.example.rent_video.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private AuthorityRepository authorityRepository;

  @Autowired
  private ModelMapper modelMapper;

  public List<CustomerResponse> findAll() {
    return customerRepository.findAll().stream()
        .map(customerEntity -> modelMapper.map(customerEntity, CustomerResponse.class))
        .toList();
  }

  public CustomerResponse findById(Long id) {
    CustomerEntity customerEntity = getCustomerEntity(id);
    return modelMapper.map(customerEntity, CustomerResponse.class);
  }

  private CustomerEntity getCustomerEntity(Long id) {
    CustomerEntity customerEntity = customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);

    Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    String role = authentication.getAuthorities().stream().toList().getFirst().getAuthority();
    if (!customerEntity.getEmail().equals(email) &&
        role.equals("ROLE_CUSTOMER")) {
      throw new UnauthorizedAccessException();
    }
    return customerEntity;
  }

  public CustomerResponse save(CustomerRequest customerRequest) {
    if (customerRepository.findOneByEmail(customerRequest.getEmail()).isPresent()) {
      throw new CustomerAlreadyExistsException();
    }

    Optional<AuthorityEntity> authorityEntityOptional = authorityRepository.findByName(customerRequest.getRole());
    AuthorityEntity authorityEntity;
    if (authorityEntityOptional.isEmpty()) {
      authorityEntity = new AuthorityEntity();
      authorityEntity.setName(customerRequest.getRole());
      authorityEntity = authorityRepository.save(authorityEntity);
    } else {
      authorityEntity = authorityEntityOptional.get();
    }

    CustomerEntity customerEntity = modelMapper.map(customerRequest, CustomerEntity.class);
    customerEntity.setAuthorityEntity(authorityEntity);

    customerEntity = customerRepository.save(customerEntity);

    CustomerResponse customerResponse = modelMapper.map(customerEntity, CustomerResponse.class);
    customerResponse.setRole(customerRequest.getRole());
    return customerResponse;
  }


  public CustomerResponse findOneByEmail(String email) {
    return customerRepository.findOneByEmail(email)
        .map(customerEntity -> modelMapper.map(customerEntity, CustomerResponse.class))
        .orElseThrow(CustomerNotFoundException::new);
  }

  public List<VideoResponse> getRentedVideos(Long id) {
    CustomerEntity customerEntity = getCustomerEntity(id);
    return customerEntity
        .getVideoEntityList().stream()
        .map(videoEntity -> modelMapper.map(videoEntity, VideoResponse.class))
        .toList();
  }

}
