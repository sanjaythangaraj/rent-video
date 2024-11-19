package com.example.rent_video.exception.video;

public class VideoRentalLimitReachedException extends RuntimeException{

  public VideoRentalLimitReachedException() {
    super("Video rental limit of 2 already reached");
  }

  public VideoRentalLimitReachedException(String message) {
    super(message);
  }
}
