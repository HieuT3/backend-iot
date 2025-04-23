package com.backend.system.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "warnings", indexes = {
        @Index(name = "idx_timestamp", columnList = "timestamp")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Warning extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long warningId;

    @Column(nullable = false)
    LocalDateTime timestamp;

    String imagePath;

    String info;
}
