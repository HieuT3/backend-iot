package com.backend.system.service.impl;

import com.backend.system.dto.request.PeopleRequest;
import com.backend.system.dto.response.PeopleResponse;
import com.backend.system.entity.Notification;
import com.backend.system.entity.People;
import com.backend.system.exception.AppException;
import com.backend.system.exception.ErrorCode;
import com.backend.system.mapper.PeopleMapper;
import com.backend.system.repository.PeopleRepository;
import com.backend.system.service.CloudinaryService;
import com.backend.system.service.NotificationService;
import com.backend.system.service.PeopleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PeopleServiceImpl implements PeopleService {

    PeopleRepository peopleRepository;
    PeopleMapper peopleMapper;
    CloudinaryService cloudinaryService;
    NotificationService notificationService;
    ObjectMapper objectMapper;

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

    private String normalizeCapital(String name) {
        return Arrays.stream(name.split("\\s+"))
                .map(word ->
                        Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    private String normalizeUnMark(String name) {
        return Normalizer.normalize(name, java.text.Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    private String generateIdentificationId(String name) {
        System.out.println(name);
        String[] names = name.split("\\s+");
        List<People> people = peopleRepository.getAllByNameEndsWith(names[names.length - 1])
                .orElse(List.of());
        int count = 0;
        if (people.isEmpty()) count = 1;
        else count = people.size() + 1;

        StringBuilder sb = new StringBuilder();
        sb.append(normalizeUnMark(names[names.length - 1]));
        for (int i = 0; i < names.length - 1; i++) sb.append(names[i].charAt(0));
        String index = Integer.toString(count);
        sb.append(index.length() == 1 ? ("0" + index) : index);
        return sb.toString();
    }

    private void sendMessage(String identificationId, String imagePath, long peopleId) throws JsonProcessingException {
        Map<String, Object> map = Map.of(
		"identificationId", identificationId,
               "image_path", imagePath,
               "peopleId", peopleId
        );
        Notification<Object> notification = Notification.builder()
                .message("ADD_PEOPLE")
                .data(map)
                .build();

        notificationService.sendMessage("/topic/messages", notification);
    }

    @Override
    public PeopleResponse addPeople(PeopleRequest peopleRequest) {
        try {
            People people = new People();
            String name = normalizeCapital(peopleRequest.getName());
            String identificationId = generateIdentificationId(name);
            String imagePath = cloudinaryService.uploadMultipartFile(peopleRequest.getFile());

            people.setName(name);
            people.setIdentificationId(identificationId);
            people.setGender(peopleRequest.getGender());
            people.setBirthday(peopleRequest.getBirthday());
            people.setFaceImagePath(imagePath);

            

            people = peopleRepository.save(people);

            sendMessage(identificationId, imagePath, people.getPeopleId());

            return peopleMapper.toPeopleResponse(people);
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
            existingPeople.setGender(peopleRequest.getGender());
            existingPeople.setBirthday(peopleRequest.getBirthday());
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
