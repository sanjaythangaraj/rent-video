package com.example.rent_video.exception.video;

public class VideoNotRentedException extends RuntimeException {
  public VideoNotRentedException() {
    super("Video has not been rented yet or has been returned");
  }

  public VideoNotRentedException(String message) {
    super(message);
  }
}
