package com.example.rent_video.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class VideoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String director;

  private String genre;

  private Boolean isAvailable;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private CustomerEntity customerEntity;
}
