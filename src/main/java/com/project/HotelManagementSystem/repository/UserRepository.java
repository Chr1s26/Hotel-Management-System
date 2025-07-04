package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByNameIgnoreCase(@NotBlank(message = "Name cannot be empty.") String name);

    boolean existsByName(String name);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);
}
