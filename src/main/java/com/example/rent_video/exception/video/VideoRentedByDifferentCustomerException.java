package com.example.rent_video.exception.video;

public class VideoRentedByDifferentCustomerException extends RuntimeException {
  public VideoRentedByDifferentCustomerException() {
    super("Can't return a video that hasn't been rented by you");
  }

  public VideoRentedByDifferentCustomerException(String message) {
    super(message);
  }
}
