package com.backend.system.mapper;

import com.backend.system.dto.response.UserResponse;
import com.backend.system.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PeopleMapper.class})
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
