package com.backend.system.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "people")
public class People extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "people_id")
    Long peopleId;

    @Column(nullable = false)
    String name;

    int age;

    @Column(name = "face_image_path")
    String faceImagePath;

    @OneToMany(mappedBy = "people", orphanRemoval = true)
    Set<History> histories = new HashSet<>();
}
