package com.backend.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class HistoryRequest {

    @NotNull
    LocalDateTime timestamp;

    Long peopleId;

    String imagePath;

    @NotBlank
    String mode;
}
