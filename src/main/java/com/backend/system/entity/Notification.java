package com.backend.system.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Notification <T>{
    private String message;
    T data;
}
