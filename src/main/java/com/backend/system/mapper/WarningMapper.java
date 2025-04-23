package com.backend.system.mapper;

import com.backend.system.dto.response.WarningResponse;
import com.backend.system.entity.Warning;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WarningMapper {
    WarningResponse covertToWarningResponse(Warning warning);
}
