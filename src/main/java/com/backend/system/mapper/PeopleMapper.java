package com.backend.system.mapper;

import com.backend.system.dto.response.PeopleResponse;
import com.backend.system.entity.People;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PeopleMapper {
    PeopleResponse toPeopleResponse(People people);
}
