package com.backend.system.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarningResponse {

    Long warningId;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    LocalDateTime timestamp;
    String imagePath;
    String info;
}
