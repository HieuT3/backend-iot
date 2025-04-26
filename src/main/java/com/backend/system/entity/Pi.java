package com.backend.system.entity;

import com.backend.system.constant.ModeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pi")
public class Pi extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long piId;

    private String description;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ModeType mode;
}
