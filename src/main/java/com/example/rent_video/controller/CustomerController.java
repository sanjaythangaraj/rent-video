package com.example.rent_video.controller;

import com.example.rent_video.exchange.customer.CustomerResponse;
import com.example.rent_video.exchange.video.VideoResponse;
import com.example.rent_video.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @GetMapping
  public ResponseEntity<List<CustomerResponse>>findAll() {
    List<CustomerResponse> response = customerService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
    CustomerResponse response = customerService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{id}/videos")
  public ResponseEntity<List<VideoResponse>> getRentedVideos(@PathVariable Long id) {
    List<VideoResponse> list = customerService.getRentedVideos(id);
    return ResponseEntity.status(HttpStatus.OK).body(list);
  }
}
