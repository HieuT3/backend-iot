package com.backend.system.mapper;

import com.backend.system.dto.response.HistoryResponse;
import com.backend.system.entity.History;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PeopleMapper.class})
public interface HistoryMapper {
    HistoryResponse toHistoryResponse(History history);
}
