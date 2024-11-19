package com.example.rent_video.exception.customer;

public class CustomerAlreadyExistsException extends RuntimeException{
  public CustomerAlreadyExistsException() {
    super("Customer with given email already exists");
  }

  public CustomerAlreadyExistsException(String message) {
    super(message);
  }
}
