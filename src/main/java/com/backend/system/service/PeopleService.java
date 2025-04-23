package com.backend.system.service;

import com.backend.system.dto.request.PeopleRequest;
import com.backend.system.dto.response.PeopleResponse;
import com.backend.system.entity.People;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PeopleService {
    Page<PeopleResponse> getAll(int page, int limit);
    PeopleResponse getPeopleDtoById(Long peopleId);
    PeopleResponse addPeople(PeopleRequest peopleRequest);
    PeopleResponse updatePeopleById(Long peopleId, PeopleRequest peopleRequest);
    void deletePeopleById(Long peopleId);
    Optional<People> getOptionalPeopleById(Long peopleId);
    People getPeopleEntityById(Long peopleId);
}
