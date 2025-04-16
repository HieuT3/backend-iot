package com.backend.system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PeopleResponse {
    Long peopleId;
    String name;
    int age;
    String faceImagePath;
}
