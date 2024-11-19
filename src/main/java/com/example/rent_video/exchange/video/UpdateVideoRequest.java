package com.example.rent_video.exchange.video;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class UpdateVideoRequest {
  private String title;

  private String director;

  private String genre;

  private Boolean isAvailable;
}
