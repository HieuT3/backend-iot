package com.backend.system.service.impl;

import com.backend.system.dto.request.PeopleRequest;
import com.backend.system.dto.response.PeopleResponse;
import com.backend.system.entity.People;
import com.backend.system.exception.AppException;
import com.backend.system.exception.ErrorCode;
import com.backend.system.mapper.PeopleMapper;
import com.backend.system.repository.PeopleRepository;
import com.backend.system.service.CloudinaryService;
import com.backend.system.service.PeopleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PeopleServiceImpl implements PeopleService {

    PeopleRepository peopleRepository;
    PeopleMapper peopleMapper;
    CloudinaryService cloudinaryService;

    @Override
    public Page<PeopleResponse> getAll(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<People> people = peopleRepository.findAll(pageable);
        return people.map(peopleMapper::toPeopleResponse);
    }

    @Override
    public PeopleResponse getPeopleDtoById(Long peopleId) {
        People people = getPeopleEntityById(peopleId);
        return peopleMapper.toPeopleResponse(people);
    }

    @Override
    public PeopleResponse addPeople(PeopleRequest peopleRequest) {
        try {
            People people = new People();
            people.setName(peopleRequest.getName());
            people.setAge(peopleRequest.getAge());
            people.setFaceImagePath(cloudinaryService.uploadMultipartFile(peopleRequest.getFile()));
            return peopleMapper.toPeopleResponse(peopleRepository.save(people));
        } catch (IOException e) {
            log.error("Error uploading image to Cloudinary: {}", e.getMessage());
            throw new AppException(ErrorCode.IMAGE_UPLOAD_ERROR);
        }
    }

    @Override
    public PeopleResponse updatePeopleById(Long peopleId, PeopleRequest peopleRequest) {
        try {
            People existingPeople = getPeopleEntityById(peopleId);
            existingPeople.setName(peopleRequest.getName());
            if (peopleRequest.getAge() != 0) existingPeople.setAge(peopleRequest.getAge());
            if (peopleRequest.getFile() != null) existingPeople.setFaceImagePath(cloudinaryService.uploadMultipartFile(peopleRequest.getFile()));
            return peopleMapper.toPeopleResponse(
                    peopleRepository.save(existingPeople)
            );
        } catch (IOException e) {
            log.error("Error uploading image to Cloudinary: {}", e.getMessage());
            throw new AppException(ErrorCode.IMAGE_UPLOAD_ERROR);
        }
    }

    @Override
    public void deletePeopleById(Long peopleId) {
        People people = getPeopleEntityById(peopleId);
        peopleRepository.delete(people);
    }

    @Override
    public Optional<People> getOptionalPeopleById(Long peopleId) {
        return peopleRepository.findById(peopleId);
    }

    @Override
    public People getPeopleEntityById(Long peopleId) {
        return peopleRepository.findById(peopleId)
                .orElseThrow(() -> new AppException(ErrorCode.PEOPLE_NOT_FOUND));
    }
}
