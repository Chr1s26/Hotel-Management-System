package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByNameIgnoreCase(String name);
    Boolean existsByNameIgnoreCase(String name);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
}
