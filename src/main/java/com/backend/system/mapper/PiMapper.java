package com.backend.system.mapper;

import com.backend.system.dto.response.PiResponse;
import com.backend.system.entity.Pi;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PiMapper {
    PiResponse convertToPiResponse(Pi pi);
}
