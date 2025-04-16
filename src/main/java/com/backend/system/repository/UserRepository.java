package com.backend.system.repository;

import com.backend.system.entity.People;
import com.backend.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    boolean existsUserByPeople(People people);

    @Query(value = "select u.registrationToken from User u")
    List<String> findAllRegistrationToken();
}
