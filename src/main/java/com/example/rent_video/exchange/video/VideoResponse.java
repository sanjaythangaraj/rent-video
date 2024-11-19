package com.example.rent_video.exchange.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class VideoResponse {
  private Long id;

  private String title;

  private String director;

  private String genre;

  private Boolean isAvailable;
}
