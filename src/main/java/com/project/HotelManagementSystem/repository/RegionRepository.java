package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Region;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region,Long> {
    Optional<Region> findByNameIgnoreCase(@NotBlank(message = "Region name can't be empty.") String name);
}
