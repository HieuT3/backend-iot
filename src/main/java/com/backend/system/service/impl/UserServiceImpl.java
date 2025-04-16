package com.backend.system.service.impl;

import com.backend.system.dto.request.RegistrationTokenRequest;
import com.backend.system.dto.request.UserRequest;

import com.backend.system.dto.response.RegistrationTokenResponse;
import com.backend.system.dto.response.UserResponse;
import com.backend.system.entity.People;
import com.backend.system.entity.User;
import com.backend.system.exception.AppException;
import com.backend.system.exception.ErrorCode;
import com.backend.system.mapper.UserMapper;
import com.backend.system.repository.UserRepository;
import com.backend.system.service.PeopleService;
import com.backend.system.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PeopleService peopleService;
    PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();

    }

    @Override
    public UserResponse getUserById(Long userId) {
        return userMapper.toUserResponse(getUserEntityById(userId));
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        if (userRepository.findUserByUsername(userRequest.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        People people = getPeopleIfSpecified(userRequest.getPeopleId());

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPeople(people);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    private People getPeopleIfSpecified(Long peopleId) {
        if (peopleId == null) {
            return null;
        }
        People people = peopleService.getOptionalPeopleById(peopleId)
                .orElse(null);
        if (people != null && userRepository.existsUserByPeople(people)) {
            throw new AppException(ErrorCode.PEOPLE_ALREADY_EXISTS);
        }
        return people;
    }

    @Override
    public UserResponse updateUserById(Long userId, UserRequest userRequest) {
        User existingUserById = getUserEntityById(userId);
        User existingUserByUserName = userRepository.findUserByUsername(userRequest.getUsername())
                .orElse(null);
        if (existingUserByUserName != null && !Objects.equals(existingUserByUserName.getUserId(), existingUserById.getUserId())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        existingUserById.setUsername(userRequest.getUsername());
        existingUserById.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return userMapper.toUserResponse(
                userRepository.save(existingUserById)
        );
    }

    @Override
    public void deleteUserById(Long userId) {
        User user = getUserEntityById(userId);
        userRepository.delete(user);
    }

    @Override
    public List<RegistrationTokenResponse> getToken() {
        return userRepository.findAllRegistrationToken()
                .stream()
                .map(RegistrationTokenResponse::new)
                .toList();
    }

    @Override
    public RegistrationTokenResponse getRegistrationTokenByUserId(Long userId) {
        User user = getUserEntityById(userId);
        return new RegistrationTokenResponse(user.getRegistrationToken());
    }

    @Override
    public void updateRegistrationTokenByUserId(Long userId, RegistrationTokenRequest registrationTokenRequest) {
        User user = getUserEntityById(userId);
        user.setRegistrationToken(registrationTokenRequest.getToken());
        userRepository.save(user);

    }

    private User getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
