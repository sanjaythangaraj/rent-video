package com.example.rent_video.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class CustomerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String email;
  private String password;
  private String firstName;
  private String lastName;
  @ManyToOne
  @JoinColumn(name = "authority_entity_id")
  private AuthorityEntity authorityEntity;

  @OneToMany(mappedBy = "customerEntity")
  private List<VideoEntity> videoEntityList = new ArrayList<>();
}
