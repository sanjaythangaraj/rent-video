package com.example.rent_video.exception.video;

public class VideoNotAvailableException extends RuntimeException{

  public VideoNotAvailableException() {
    super("Video currently not available to rent");
  }

  public VideoNotAvailableException(String message) {
    super(message);
  }
}
