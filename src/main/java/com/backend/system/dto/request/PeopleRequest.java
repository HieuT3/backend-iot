package com.backend.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PeopleRequest {

    @NotBlank(message = "Name is required")
    String name;

    @NotBlank(message = "Gender is required")
    String gender;

    @NotBlank(message = "Birthday is required")
    String birthday;

    MultipartFile file;
}
