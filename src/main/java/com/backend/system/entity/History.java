package com.backend.system.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "histories", indexes = {
        @Index(name = "idx_timestamp", columnList = "timestamp"),
        @Index(name = "idx_people_id", columnList = "people_id")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class History extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long historyId;

    @Column(nullable = false)
    LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "people_id", referencedColumnName = "people_id")
    People people;

    String imagePath;

    String mode;
}
