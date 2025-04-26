package com.backend.system.dto.request;

import com.backend.system.constant.ModeType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PiRequest {

    String description;

    @NotNull(message = "Mode type cannot be null")
    ModeType mode;
}
