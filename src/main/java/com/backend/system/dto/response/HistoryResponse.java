package com.backend.system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryResponse {
    Long historyId;
    LocalDateTime timestamp;
    PeopleResponse people;
    String imagePath;
    String mode;
}
