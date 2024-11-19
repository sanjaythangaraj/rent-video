package com.example.rent_video.repository;

import com.example.rent_video.data.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

  List<VideoEntity> findAllByIsAvailableIsTrue();
}
