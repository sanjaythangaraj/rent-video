package com.example.rent_video.controller;

import com.example.rent_video.exchange.video.CreateVideoRequest;
import com.example.rent_video.exchange.video.UpdateVideoRequest;
import com.example.rent_video.exchange.video.VideoResponse;
import com.example.rent_video.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

  @Autowired
  private VideoService videoService;

  @GetMapping
  public ResponseEntity<List<VideoResponse>> findAllVideos(@RequestParam(required = false) String availability) {
    List<VideoResponse> videoResponses;
    if (availability != null && availability.equalsIgnoreCase("available")) {
      videoResponses = videoService.findAllAvailableVideos();
    } else {
      videoResponses = videoService.findAll();
    }
    return ResponseEntity.status(HttpStatus.OK).body(videoResponses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<VideoResponse> findByVideoId(@PathVariable Long id) {
    VideoResponse response = videoService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping
  public ResponseEntity<VideoResponse> createVideo(@RequestBody @Valid CreateVideoRequest createVideoRequest) {
    VideoResponse response = videoService.save(createVideoRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<VideoResponse> updateVideo(@PathVariable Long id, @RequestBody @Valid UpdateVideoRequest updateVideoRequest) {
    VideoResponse response = videoService.update(id, updateVideoRequest);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<VideoResponse> deleteVideo(@PathVariable Long id) {
    videoService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/rent")
  public ResponseEntity<VideoResponse> rentVideo(@PathVariable Long id) {
    VideoResponse response = videoService.rentVideo(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/{id}/return")
  public ResponseEntity<VideoResponse> returnVideo(@PathVariable Long id) {
    VideoResponse response = videoService.returnVideo(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }



}
