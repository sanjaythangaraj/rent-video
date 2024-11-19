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
public class CreateVideoRequest {

  @NotBlank(message = "Title is mandatory")
  private String title;

  @NotBlank(message = "Director is mandatory")
  private String director;

  @NotBlank(message = "Genre is mandatory")
  private String genre;

  private Boolean isAvailable;
}
