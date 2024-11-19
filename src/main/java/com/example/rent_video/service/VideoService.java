package com.example.rent_video.service;

import com.example.rent_video.data.CustomerEntity;
import com.example.rent_video.data.VideoEntity;
import com.example.rent_video.exception.customer.CustomerNotFoundException;
import com.example.rent_video.exception.video.*;
import com.example.rent_video.exchange.customer.CustomerResponse;
import com.example.rent_video.exchange.video.CreateVideoRequest;
import com.example.rent_video.exchange.video.UpdateVideoRequest;
import com.example.rent_video.exchange.video.VideoResponse;
import com.example.rent_video.repository.CustomerRepository;
import com.example.rent_video.repository.VideoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {
  @Autowired
  private VideoRepository videoRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CustomerRepository customerRepository;

  public List<VideoResponse> findAll() {
    List<VideoEntity> videoEntities = videoRepository.findAll();
    return videoEntities
        .stream()
        .map(videoEntity -> modelMapper.map(videoEntity, VideoResponse.class)).toList();
  }

  public List<VideoResponse> findAllAvailableVideos() {
    return videoRepository.findAllByIsAvailableIsTrue().stream()
        .map(videoEntity -> modelMapper.map(videoEntity, VideoResponse.class))
        .toList();
  }

  public VideoResponse findById(Long id) {
    return videoRepository.findById(id)
        .map(videoEntity -> modelMapper.map(videoEntity, VideoResponse.class))
        .orElseThrow(VideoNotFoundException::new);
  }

  public VideoResponse save(CreateVideoRequest createVideoRequest) {
    VideoEntity videoEntity = modelMapper.map(createVideoRequest, VideoEntity.class);
    if (videoEntity.getIsAvailable() == null) videoEntity.setIsAvailable(true);
    videoEntity = videoRepository.save(videoEntity);
    return modelMapper.map(videoEntity, VideoResponse.class);
  }

  public VideoResponse update(Long id, UpdateVideoRequest updateVideoRequest) {
    VideoEntity videoEntity = videoRepository.save(mapToVideoEntity(id, updateVideoRequest));
    return modelMapper.map(videoEntity, VideoResponse.class);
  }

  public VideoResponse setVideoAvailability(Long id, Boolean isAvailable) {
    VideoEntity videoEntity = videoRepository.findById(id).orElseThrow(VideoNotFoundException::new);
    videoEntity.setIsAvailable(isAvailable);
    videoEntity = videoRepository.save(videoEntity);
    return modelMapper.map(videoEntity, VideoResponse.class);
  }

  public VideoResponse rentVideo(Long id) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    CustomerEntity customerEntity = customerRepository.findOneByEmail(email)
        .orElseThrow(CustomerNotFoundException::new);

    VideoEntity videoEntity = videoRepository.findById(id).orElseThrow(VideoNotFoundException::new);
    if (!videoEntity.getIsAvailable()) throw new VideoNotAvailableException();

    if (customerEntity.getVideoEntityList().size() >= 2) throw new VideoRentalLimitReachedException();

    videoEntity.setIsAvailable(false);
    videoEntity.setCustomerEntity(customerEntity);
    videoEntity = videoRepository.save(videoEntity);
    return modelMapper.map(videoEntity, VideoResponse.class);
  }

  public VideoResponse returnVideo(Long id) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();

    VideoEntity videoEntity = videoRepository.findById(id).orElseThrow(VideoNotFoundException::new);
    if (videoEntity.getIsAvailable()) throw new VideoNotRentedException();

    if (!videoEntity.getCustomerEntity().getEmail().equalsIgnoreCase(email)) {
      throw new VideoRentedByDifferentCustomerException();
    }

    videoEntity.setIsAvailable(true);
    videoEntity.setCustomerEntity(null);
    videoEntity = videoRepository.save(videoEntity);
    return modelMapper.map(videoEntity, VideoResponse.class);
  }

  public List<CustomerResponse> getBorrower(Long id) {
    VideoEntity videoEntity = videoRepository.findById(id).orElseThrow(VideoNotFoundException::new);
    if (videoEntity.getIsAvailable()) return List.of();

    return List.of(modelMapper.map(videoEntity.getCustomerEntity(), CustomerResponse.class));
  }

  public void deleteById(Long id) {
    findById(id);
    videoRepository.deleteById(id);
  }

  private VideoEntity mapToVideoEntity(Long id, UpdateVideoRequest updateVideoRequest) {
    VideoEntity videoEntity = modelMapper.map(findById(id),VideoEntity.class);
    if (updateVideoRequest.getTitle() != null) videoEntity.setTitle(updateVideoRequest.getTitle());
    if (updateVideoRequest.getGenre() != null) videoEntity.setGenre(updateVideoRequest.getGenre());
    if (updateVideoRequest.getDirector() != null) videoEntity.setDirector(updateVideoRequest.getDirector());
    if (updateVideoRequest.getIsAvailable() != null) videoEntity.setIsAvailable(updateVideoRequest.getIsAvailable());

    return videoEntity;
  }

}
