package com.backend.system.dto.response;

import com.backend.system.constant.ModeType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PiResponse {

    Long piId;
    String description;
    ModeType mode;
}
