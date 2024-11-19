package com.example.rent_video.exception.video;

public class VideoNotFoundException extends RuntimeException{
  public VideoNotFoundException() {
    super("Video with given id not found");
  }

  public VideoNotFoundException(String message) {
    super(message);
  }
}
