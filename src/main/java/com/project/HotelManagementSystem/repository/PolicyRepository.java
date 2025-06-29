package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy,Long> {
    Optional<Policy> findByTitleAndDescription(String title, String description);
}
