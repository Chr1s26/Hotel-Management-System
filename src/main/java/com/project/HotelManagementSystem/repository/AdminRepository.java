package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Admin;
import com.project.HotelManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByNameIgnoreCase(String name);
    Optional<Admin> findByNameIgnoreCaseAndIdNot(String name, Long id);
    Optional<Admin> findByUser(User user);
}
